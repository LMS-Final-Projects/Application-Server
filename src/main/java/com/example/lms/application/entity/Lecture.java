package com.example.lms.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    private Long lectureId;
    private String memberId;
    private String lectureName;
    private String professorName;
    private Long maximumNumber;
    private Integer score;
    private Integer startTime; //시작 시간
    private Integer year;
    private Semester semester;
    @ElementCollection
    private List<Integer> classTimes; // 해당 교시
    private DayOfWeek dayOfWeek;
}
