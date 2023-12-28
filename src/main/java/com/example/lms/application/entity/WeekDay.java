package com.example.lms.application.entity;


import com.example.lms.global.kafka.KafkaAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String memberId;

    @Column
    private DayOfWeek dayOfWeek; //월, 화, 수, 목, 금

    @OneToMany
    private List<Lecture> lectures; //과목 정보들.
}