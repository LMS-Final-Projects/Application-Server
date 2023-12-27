package com.example.lms.application.repository;

import com.example.lms.application.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member as m where m.id = :memberId")
    Optional<Member> findByMemberId(@Param("memberId") String memberId);
}
