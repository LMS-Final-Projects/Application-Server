package com.example.lms.application.dto;

import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Status;
import lombok.Getter;

@Getter
public class ApplicationResponse {
    private Long id;
    private Long lectureId;
    private String lectureName;
    private String professorName;
    private Integer score;
    private Long maximumNumber;
    private Status status;

    public ApplicationResponse(Application application) {
        this.id = application.getId();
        this.lectureId = application.getLectureId();
        this.lectureName = application.getLectureName();
        this.professorName = application.getProfessorName();
        this.score = application.getScore();
        this.maximumNumber = application.getMaximumNumber();
        this.status = application.getStatus();
    }

}
