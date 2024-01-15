package com.example.lms.application.dto;

import com.example.lms.application.entity.Application;
import lombok.Getter;

@Getter
public class ManageApplicationResponse {
    private Integer id;
    private Integer lectureId;
    private String lectureName;
    private String professorName;
    private Integer score;
    private Integer maximumNumber;
    private String memberId;
    private String name;

    public ManageApplicationResponse(Application application) {
        this.id = application.getId();
        this.lectureId = application.getLectureId();
        this.lectureName = application.getLectureName();
        this.professorName = application.getProfessorName();
        this.score = application.getScore();
        this.maximumNumber = application.getMaximumNumber();
        this.memberId = application.getMember().getId();
        this.name = application.getMember().getName();
    }

}
