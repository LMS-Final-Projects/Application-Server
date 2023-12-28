package com.example.lms.application.dto;

import com.example.lms.application.entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {
    private String memberId;
    private Semester semester;
    private Integer year;

}