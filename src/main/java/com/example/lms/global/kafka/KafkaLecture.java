package com.example.lms.global.kafka;
import com.example.lms.application.entity.WeekDay;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaLecture {

    private String memberId;
    private Long lectureId;
    private String lectureName;
    private String professorName;
    private Integer score;
    private Integer startTime;
    private WeekDay weekday;
    private KafkaAction kafkaAction;

}

