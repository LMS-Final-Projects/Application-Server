package com.example.lms.application.service;

import com.example.lms.application.api.ScheduleServerClient;
import com.example.lms.application.dto.ApplicationAcceptRequest;
import com.example.lms.application.dto.ManageApplicationResponse;
import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Lecture;
import com.example.lms.application.entity.Status;
import com.example.lms.application.entity.WeekDay;
import com.example.lms.application.repository.ApplicationRepository;
import com.example.lms.application.repository.LectureRepository;
import com.example.lms.application.repository.WeekdayRepository;
import com.example.lms.global.exception.DuplicateException;
import com.example.lms.global.exception.MethodException;
import com.example.lms.global.exception.NotFoundException;
import com.example.lms.global.kafka.KafkaAction;
import com.example.lms.global.kafka.KafkaLecture;
import com.example.lms.global.kafka.KafkaProducer;
import com.example.lms.global.kafka.KafkaWeekDay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ManageService {

    private final WeekdayRepository weekdayRepository;
    private final LectureRepository lectureRepository;
    private final ApplicationRepository applicationRepository;
    private final KafkaProducer kafkaProducer;
    private final ScheduleServerClient scheduleServerClient;

    @Transactional
    public List<ManageApplicationResponse> getListAll() {
        List<Application> applicationList = applicationRepository.findByStatus(Status.PENDING);
        return applicationList.stream()
                .map(application -> new ManageApplicationResponse(application))
                .toList();
    }

    @Transactional
    public void accept(ApplicationAcceptRequest request) {
        Application application = applicationRepository.findByMemberIdAndId(request.getMemberId(),request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("권한이 없거나 없는 수강신청 입니다.") );

        System.out.println("1 :" +application);

        List<Integer> classTimes = new ArrayList<>();
        int start = application.getStartTime();
        int end = application.getStartTime() + application.getScore() - 1;


        classTimes.addAll(IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList()));

        Lecture lecture = lectureRepository.findById(request.getLectureId()).orElseThrow(() -> new NotFoundException("해당 과목 정보가 없습니다."));

        System.out.println("2");

        List<WeekDay> byMemberId = weekdayRepository.findByMemberId(request.getMemberId());

        WeekDay weekDayUpdate = WeekDay.builder()
                .memberId(request.getMemberId())
                .dayOfWeek(application.getDayOfWeek())
                .lectureId(lecture.getLectureId())
                .build();

        System.out.println("----------문제발생 ----------");
        if(!byMemberId.isEmpty()){
        for(WeekDay test:byMemberId) {
            if (lecture.getDayOfWeek().equals(test.getDayOfWeek()) || lecture.getClassTimes().stream().anyMatch(classTimes::contains)) {
                throw new DuplicateException("이미 해당 시간에 수업이 있습니다.");
            }
        }}
        System.out.println("----------문제해결 ----------");

        weekdayRepository.save(weekDayUpdate);

        System.out.println("3");

        KafkaLecture kafkaLecture = KafkaLecture.builder()
                .memberId(request.getMemberId())
                .lectureId(lecture.getLectureId())
                .professorName(lecture.getProfessorName())
                .lectureName(lecture.getLectureName())
                .score(lecture.getScore())
                .startTime(lecture.getStartTime())
                .maximumNumber(lecture.getMaximumNumber())
                .classTimes(classTimes)
                .dayOfWeek(lecture.getDayOfWeek())
                .year(lecture.getYear())
                .semester(lecture.getSemester())
                .kafkaAction(KafkaAction.CREATE)
                .build();

        KafkaWeekDay kafkaWeekDay = KafkaWeekDay.builder()
                .memberId(request.getMemberId())
                .id(weekDayUpdate.getId())
                .dayOfWeek(weekDayUpdate.getDayOfWeek())
                .lectureId(weekDayUpdate.getLectureId())
                .kafkaAction(KafkaAction.CREATE)
                .build();

        kafkaProducer.saveLecture("lecture", kafkaLecture);
        kafkaProducer.saveWeekDay("weekday", kafkaWeekDay);

        System.out.println("4");

        application.setStatus(Status.ACCEPTED);
        System.out.println("5");

        System.out.println("강의 승인 성공");
        System.out.println("스케줄 저장 성공");
    }


    @Transactional
    public void reject(ApplicationAcceptRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("없는 수강신청 입니다.") );
        application.setStatus(Status.REJECTED);
    }


}
