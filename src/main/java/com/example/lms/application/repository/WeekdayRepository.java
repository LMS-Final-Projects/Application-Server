package com.example.lms.application.repository;

import com.example.lms.application.entity.Member;
import com.example.lms.application.entity.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeekdayRepository extends JpaRepository<WeekDay, Long> {

    @Query("select w FROM WeekDay as w where w.memberId = :memberId")
    List<WeekDay> findByMemberId(@Param("memberId")String memberId);
}
