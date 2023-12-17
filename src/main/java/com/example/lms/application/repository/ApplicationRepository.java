package com.example.lms.application.repository;

import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByLectureIdAndMember(Long lectureId, Member member);
}
