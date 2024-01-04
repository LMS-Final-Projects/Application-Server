package com.example.lms.application.service;

import com.example.lms.application.dto.ApplicationCancelRequest;
import com.example.lms.application.dto.ApplicationRequest;
import com.example.lms.application.dto.ApplicationResponse;
import com.example.lms.application.dto.DeleteRequest;
import com.example.lms.application.entity.*;
import com.example.lms.application.repository.ApplicationRepository;
import com.example.lms.application.repository.CheckRepository;
import com.example.lms.application.repository.LectureRepository;
import com.example.lms.application.repository.MemberRepository;
import com.example.lms.global.exception.DuplicateException;
import com.example.lms.global.exception.NotFoundException;
import com.example.lms.global.kafka.KafkaAction;
import com.example.lms.global.kafka.KafkaLecture;
import com.example.lms.global.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final LectureRepository lectureRepository;
    private final CheckRepository checkRepository;
    private final MemberRepository memberRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public List<ApplicationResponse> getList(String memberId, String role, String name, boolean accept) {

        System.out.println("memberId: " + memberId);
        System.out.println("role: " + role);
        System.out.println("name: " + name);
        System.out.println("accept: " + accept);

        List<Application> applicationList = null;

        if (!accept) {
            applicationList = applicationRepository.findByMemberIdAndStatus(memberId, Status.PENDING);
        } else if("STUDENT".equals(role)){
            applicationList = applicationRepository.findByMemberId(memberId);
        } else {
            applicationList = applicationRepository.findByProfessorName(name);
        }

        return applicationList.stream()
                .map(application -> new ApplicationResponse(application))
                .toList();
    }




    @Transactional
    public void apply(ApplicationRequest request, String memberId) {

        Long count = checkRepository.increment(request.getLectureId());
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException("일치하는 회원이 없습니다.")
        );;

        Lecture lecture = lectureRepository.findByLectureId(request.getLectureId()).orElseThrow(
                () -> new NotFoundException("과목이 없습니다."));

        if (count > lecture.getMaximumNumber()) {
            System.out.println("1");
            return;
        }

        System.out.println("2");

        Application application = Application.builder()
                .lectureId(request.getLectureId())
                .lectureName(lecture.getLectureName())
                .professorName(lecture.getProfessorName())
                .score(lecture.getScore())
                .maximumNumber(lecture.getMaximumNumber())
                .startTime(lecture.getStartTime())
                .dayOfWeek(lecture.getDayOfWeek())
                .status(Status.PENDING)
                .year(lecture.getYear())
                .semester(lecture.getSemester())
                .member(member)
                .build();

        System.out.println("3");
        applicationRepository.save(application);
        
    }

    @Transactional
    public void cancel(ApplicationCancelRequest request, String memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException("일치하는 회원이 없습니다.")
        );;

        Application application = applicationRepository.findByLectureIdAndMemberId(request.getLectureId(),memberId);
        System.out.println("lecture:"+request.getLectureId());
        System.out.println("member:"+memberId);
        System.out.println("------");
        System.out.println(application.getId());
        System.out.println("------");
        applicationRepository.delete(application);
        checkRepository.decrement(request.getLectureId());
    }

//    @Transactional
//    public void test(ApplicationRequest request) {
//
//
//        Long count = applicationRepository.count();
//
//        if (count > request.getMaximumNumber()) {
//            return;
//        }
//
//        Application application = Application.builder()
//                .lectureId(request.getLectureId())
//                .lectureName(request.getLectureName())
//                .professorName(request.getProfessorName())
//                .build();
//
//        applicationRepository.save(application);
//    }





    //수강신청 취소
    @Transactional
    public void deleteApplications(DeleteRequest deleteRequest) {
        applicationRepository.deleteApplicationsByIdsQuery(deleteRequest.getCourseIds());}



}
