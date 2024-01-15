package com.example.lms.application.dto;

import com.example.lms.application.entity.Application;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DeleteRequest {

    private List<Integer> courseIds;

    public List<Application> toEntities() {
        return courseIds.stream()
                .map(id -> Application.builder().id(id).build())
                .collect(Collectors.toList());
    }
}
