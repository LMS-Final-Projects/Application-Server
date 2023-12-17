package com.example.lms.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRequest {
    private Long lectureId;
    private String lectureName;
    private String professorName;
    private Long maximumNumber;
}
