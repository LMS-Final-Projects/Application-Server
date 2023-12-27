package com.example.lms.application.api;

import com.example.lms.application.dto.ScheduleRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("SCHEDULE-SERVER")
public interface ScheduleServerClient {

    @PostMapping("/api/v1/schedule")
    ResponseEntity<Void> saveSchedule(@RequestBody ScheduleRequest request);


}