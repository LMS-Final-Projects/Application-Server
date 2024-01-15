package com.example.lms.application.repository;

import com.example.lms.application.entity.Lecture;
import com.example.lms.application.entity.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    @Query("select l from Lecture as l where l.lectureId = :lectureId")
    Optional<Lecture> findByLectureId(@Param("lectureId")Integer lectureId);


}
