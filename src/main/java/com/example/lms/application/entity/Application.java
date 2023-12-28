package com.example.lms.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long lectureId;
    private String lectureName;
    private String professorName;
    private Integer score;
    private Long maximumNumber;
    private Integer startTime;
    private DayOfWeek dayOfWeek;
    private Semester semester;
    private Integer year;
    private Status status;

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
