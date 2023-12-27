package com.example.lms.application.repository;

import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByMemberId(String memberId);

    Application findByLectureIdAndMemberId(Long lectureId, String memberId);

    List<Application> findByMemberIdAndStatus(String memberId, Status accepted);

    List<Application> findByStatus(Status pending);

    @Modifying
    @Query("DELETE FROM Application m WHERE m.id IN :ids")
    void deleteApplicationsByIdsQuery(@Param("ids") List<Long> ids);
}
