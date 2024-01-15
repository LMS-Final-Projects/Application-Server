package com.example.lms.application.controller;

import com.example.lms.application.dto.ApplicationAcceptRequest;
import com.example.lms.application.dto.ManageApplicationResponse;
import com.example.lms.application.service.ManageService;
import com.example.lms.global.response.LmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/manager/application")
public class ManageController {

    private final ManageService manageService;

    // 대기중인 수강신청 조회
    @GetMapping
    public LmsResponse<List<ManageApplicationResponse>> getListAll(){
        List<ManageApplicationResponse> response = manageService.getListAll();
        return new LmsResponse<>(HttpStatus.OK, response, "조회 성공", "", LocalDateTime.now());
    }

    // 수강신청 승인
    @PostMapping("/accepted")
    public void accept(@RequestBody ApplicationAcceptRequest request){
        manageService.accept(request);
    }

    // 수강신청 거절
    @PostMapping("/rejected")
    public void reject(@RequestBody ApplicationAcceptRequest request){
        manageService.reject(request);
    }
}
