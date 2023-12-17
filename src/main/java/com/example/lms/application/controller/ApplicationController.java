package com.example.lms.application.controller;

import com.example.lms.application.dto.ApplicationCancelRequest;
import com.example.lms.application.dto.ApplicationRequest;
import com.example.lms.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public void apply(@RequestBody ApplicationRequest request){
        applicationService.apply(request);
    }

    @DeleteMapping
    public void cancel(@RequestBody ApplicationCancelRequest request) {
        applicationService.cancel(request);
    }
}
