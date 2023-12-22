package com.example.lms.application.repository;

import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByMemberId(String memberId);

    Application findByLectureIdAndMemberId(Long lectureId, String memberId);

    List<Application> findByMemberIdAndStatus(String memberId, Status accepted);

    List<Application> findByStatus(Status pending);
}
