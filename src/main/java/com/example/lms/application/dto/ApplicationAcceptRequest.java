package com.example.lms.application.dto;

import com.example.lms.application.entity.Application;
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
public class ApplicationAcceptRequest {
    private Integer applicationId;
    private Integer lectureId;
    private String memberId;

    public ScheduleRequest toSchedule(Application application){

        ScheduleRequest build = ScheduleRequest.builder()
                .memberId(memberId)
                .year(application.getYear())
                .lectureId(application.getLectureId())
                .semester(application.getSemester())
                .build();

        return build;
    }
}
