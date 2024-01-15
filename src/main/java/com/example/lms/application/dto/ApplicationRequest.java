package com.example.lms.application.dto;

import com.example.lms.application.entity.Semester;
import com.example.lms.application.entity.WeekDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRequest {
    private Integer lectureId;
}
