package com.example.lms.application.service;

import com.example.lms.application.dto.ApplicationCancelRequest;
import com.example.lms.application.dto.ApplicationRequest;
import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Member;
import com.example.lms.application.repository.ApplicationRepository;
import com.example.lms.application.repository.CheckRepository;
import com.example.lms.application.repository.MemberRepository;
import com.example.lms.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CheckRepository checkRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void apply(ApplicationRequest request) {

        Long count = checkRepository.increment(request.getLectureId());

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

    @Transactional
    public void cancel(ApplicationCancelRequest request) {

        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(
                () -> new NotFoundException("일치하는 회원이 없습니다.")
        );;

        Application application = applicationRepository.findByLectureIdAndMember(request.getLectureId(),member);
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
