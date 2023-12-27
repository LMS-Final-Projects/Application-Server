package com.example.lms.application.controller;

import com.example.lms.application.dto.ApplicationCancelRequest;
import com.example.lms.application.dto.ApplicationRequest;
import com.example.lms.application.dto.ApplicationResponse;
import com.example.lms.application.dto.DeleteRequest;
import com.example.lms.application.service.ApplicationService;
import com.example.lms.global.response.LmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
    @RequestMapping("/api/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    // 수강신청 목록 보기
    @GetMapping
    public LmsResponse<List<ApplicationResponse>> getList(@RequestHeader(value = "member-id") String memberId){
        List<ApplicationResponse> response = applicationService.getList(memberId,false);
        return new LmsResponse<>(HttpStatus.OK, response, "조회 성공", "", LocalDateTime.now());
    }


    //수강신청 취소
    @PostMapping("/delete")
    public LmsResponse<Void> deleteApplications(@RequestBody DeleteRequest deleteRequest) {
        applicationService.deleteApplications(deleteRequest);
        return new LmsResponse<>(HttpStatus.OK, null, "서비스 성공", "", LocalDateTime.now());
    }



    // 승인된 수강신청 목록 보기
    @GetMapping("/accept")
    public LmsResponse<List<ApplicationResponse>> getAcceptList(@RequestHeader(value = "member-id") String memberId){
        List<ApplicationResponse> response = applicationService.getList(memberId,true);
        return new LmsResponse<>(HttpStatus.OK, response, "조회 성공", "", LocalDateTime.now());
    }

    // 수강 신청
    @PostMapping
    public void apply(@RequestBody ApplicationRequest request,@RequestHeader(value = "member-id") String memberId){
        applicationService.apply(request,memberId);
    }

    // 수강 신청 취소
    @DeleteMapping
    public void cancel(@RequestBody ApplicationCancelRequest request,@RequestHeader(value = "member-id") String memberId) {
        applicationService.cancel(request,memberId);
    }
}
