package com.example.lms.application.repository;

import com.example.lms.application.entity.Member;
import com.example.lms.application.entity.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekdayRepository extends JpaRepository<WeekDay, Long> {
}
