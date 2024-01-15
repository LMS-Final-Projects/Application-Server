package com.example.lms.application.repository;

import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query("select a from Application as a where a.status = com.example.lms.application.entity.Status.ACCEPTED and a.professorName = :memberName")
    List<Application> findByProfessorName(@Param("memberName")String memberName);

    List<Application> findByMemberId(String memberId);

    Application findByLectureIdAndMemberId(Integer lectureId, String memberId);

    List<Application> findByMemberIdAndStatus(String memberId, Status accepted);

    List<Application> findByStatus(Status pending);

    @Modifying
    @Query("DELETE FROM Application m WHERE m.id IN :ids")
    void deleteApplicationsByIdsQuery(@Param("ids") List<Integer> ids);

    @Query("select a from Application as a where a.member.id = :memberId and a.id = :id")
    Optional<Application> findByMemberIdAndId(@Param("memberId")String memberId, @Param("id")Integer id);
}
