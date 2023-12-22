package com.example.lms.application.service;

import com.example.lms.application.dto.ApplicationCancelRequest;
import com.example.lms.application.dto.ApplicationRequest;
import com.example.lms.application.dto.ApplicationResponse;
import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Member;
import com.example.lms.application.entity.Status;
import com.example.lms.application.repository.ApplicationRepository;
import com.example.lms.application.repository.CheckRepository;
import com.example.lms.application.repository.MemberRepository;
import com.example.lms.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CheckRepository checkRepository;
    private final MemberRepository memberRepository;

    public List<ApplicationResponse> getList(String memberId, boolean accept) {

        List<Application> applicationList = null;

        if (accept) {
            applicationList = applicationRepository.findByMemberIdAndStatus(memberId, Status.ACCEPTED);
        } else {
            applicationList = applicationRepository.findByMemberId(memberId);
        }

        return applicationList.stream()
                .map(application -> new ApplicationResponse(application))
                .toList();
    }

    @Transactional
    public void apply(ApplicationRequest request, String memberId) {

        Long count = checkRepository.increment(request.getLectureId());

        if (count > request.getMaximumNumber()) {
            System.out.println("1");
            return;
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException("일치하는 회원이 없습니다.")
        );;
        System.out.println("2");
        Application application = Application.builder()
                .lectureId(request.getLectureId())
                .lectureName(request.getLectureName())
                .professorName(request.getProfessorName())
                .score(request.getScore())
                .maximumNumber(request.getMaximumNumber())
                .status(Status.PENDING)
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

    @Transactional
    public void test(ApplicationRequest request) {

        Long count = applicationRepository.count();

        if (count > request.getMaximumNumber()) {
            return;
        }

        Application application = Application.builder()
                .lectureId(request.getLectureId())
                .lectureName(request.getLectureName())
                .professorName(request.getProfessorName())
                .build();

        applicationRepository.save(application);
    }


}
