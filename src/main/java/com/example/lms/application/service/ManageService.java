package com.example.lms.application.service;

import com.example.lms.application.dto.ApplicationAcceptRequest;
import com.example.lms.application.dto.ApplicationResponse;
import com.example.lms.application.dto.ManageApplicationResponse;
import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Status;
import com.example.lms.application.entity.WeekDay;
import com.example.lms.application.repository.ApplicationRepository;
import com.example.lms.global.exception.DuplicateException;
import com.example.lms.global.exception.NotFoundException;
import com.example.lms.global.kafka.KafkaAction;
import com.example.lms.global.kafka.KafkaLecture;
import com.example.lms.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ManageService {

    private final ApplicationRepository applicationRepository;
    private final KafkaProducer kafkaProducer;

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

        List<Integer> collect = IntStream.rangeClosed(request.getStartTime(), request.getStartTime() + request.getScore() - 1)
                .boxed()
                .collect(Collectors.toList());

        if (isTimeSlotOccupied(request.getWeekday(), collect)) {
            throw new DuplicateException("해당 시간대 요일이 이미 과목이 있습니다.");
        }

        List<String> lecturesByDay = IntStream.rangeClosed(request.getStartTime(), request.getStartTime() + request.getScore() - 1)
                .boxed()
                .map(i -> {
                    String dayOfWeek;
                    switch (i % 5) { // 5는 주중 요일의 개수 (월, 화, 수, 목, 금)
                        case 0: dayOfWeek = "monday"; break;
                        case 1: dayOfWeek = "tuesday"; break;
                        case 2: dayOfWeek = "wednesday"; break;
                        case 3: dayOfWeek = "thursday"; break;
                        case 4: dayOfWeek = "friday"; break;
                        default: throw new IllegalStateException("Unexpected value: " + i % 5);
                    }
                    return dayOfWeek + ": " + request.getLectureName();
                })
                .collect(Collectors.toList());


        KafkaLecture build = KafkaLecture.builder()
                .memberId(String.valueOf(request.getMemberId()))
                .lectureId(Long.valueOf(request.getLectureId()))
                .lectureName(request.getLectureName())
                .score(request.getScore())
                .startTime(request.getStartTime())
                .weekday(
                        WeekDay.builder()
                                .times(collect)
                                .memberId(request.getMemberId())
                                .lecturesByDay(lecturesByDay)
                                .build()
                )
                .kafkaAction(KafkaAction.CREATE)
                .build();

        kafkaProducer.saveLecture("lecture", build);

    }

    @Transactional
    public void reject(ApplicationAcceptRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("없는 수강신청 입니다.") );
        application.setStatus(Status.REJECTED);
    }

    private boolean isTimeSlotOccupied(WeekDay weekDay, List<Integer> timesToCheck) {
        List<Integer> occupiedTimes = weekDay.getTimes();
        return occupiedTimes.stream().anyMatch(timesToCheck::contains);
    }
}
