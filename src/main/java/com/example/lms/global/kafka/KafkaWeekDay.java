package com.example.lms.global.kafka;

import com.example.lms.application.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaWeekDay {

    private Long id;

    private String memberId;

    private DayOfWeek dayOfWeek;

    private Integer lectureId;

    private KafkaAction kafkaAction;
}
