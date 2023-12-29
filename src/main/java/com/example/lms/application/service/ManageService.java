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
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("없는 수강신청 입니다.") );
        application.setStatus(Status.ACCEPTED);

        List<Integer> classTimes = new ArrayList<>();
        int start = application.getStartTime();
        int end = application.getStartTime() + application.getScore() - 1;

        classTimes.addAll(IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList()));

        Lecture lecture = Lecture.builder()
                .memberId(request.getMemberId())
                .lectureId(application.getLectureId())
                .professorName(application.getProfessorName())
                .lectureName(application.getLectureName())
                .score(application.getScore())
                .startTime(application.getStartTime())
                .classTimes(classTimes)
                .dayOfWeek(application.getDayOfWeek())
                .build();
        List<Lecture> lectureList = new ArrayList<>();

        lectureList.add(lecture);

        WeekDay weekDay = WeekDay.builder()
                .memberId(request.getMemberId())
                .dayOfWeek(application.getDayOfWeek())
                .lectures(lectureList)
                .build();

        if(lecture.getDayOfWeek() == weekDay.getDayOfWeek() ||lecture.getClassTimes().stream().anyMatch(classTimes::contains)){
            throw new DuplicateException("이미 해당 시간에 수업이 있습니다.");
        }

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
                .kafkaAction(KafkaAction.CREATE)
                .build();

        KafkaWeekDay kafkaWeekDay = KafkaWeekDay.builder()
                .memberId(request.getMemberId())
                .id(weekDay.getId())
                .dayOfWeek(weekDay.getDayOfWeek())
                .lectures(weekDay.getLectures())
                .kafkaAction(KafkaAction.CREATE)
                .build();

        kafkaProducer.saveLecture("lecture", kafkaLecture);
        kafkaProducer.saveWeekDay("weekday", kafkaWeekDay);
        ResponseEntity<Void> response = scheduleServerClient.saveSchedule(request.toSchedule(application));
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new MethodException("서비스 실패");
        }

        System.out.println("스케줄 저장 성공");
    }


    @Transactional
    public void reject(ApplicationAcceptRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("없는 수강신청 입니다.") );
        application.setStatus(Status.REJECTED);
    }


}
