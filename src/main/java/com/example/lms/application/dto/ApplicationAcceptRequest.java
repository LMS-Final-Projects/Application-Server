package com.example.lms.application.dto;

import com.example.lms.application.entity.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationAcceptRequest {
    private Long applicationId;
    private Long lectureId;
    private String lectureName;
    private String professorName;
    private Long maximumNumber;
    private WeekDay weekday;
    private Integer startTime;
    private Integer score;
    private String memberId;
}
