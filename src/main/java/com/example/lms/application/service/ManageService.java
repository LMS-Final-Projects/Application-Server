package com.example.lms.application.service;

import com.example.lms.application.dto.ApplicationAcceptRequest;
import com.example.lms.application.dto.ApplicationResponse;
import com.example.lms.application.dto.ManageApplicationResponse;
import com.example.lms.application.entity.Application;
import com.example.lms.application.entity.Status;
import com.example.lms.application.repository.ApplicationRepository;
import com.example.lms.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageService {

    private final ApplicationRepository applicationRepository;

    public List<ManageApplicationResponse> getListAll() {
        List<Application> applicationList = applicationRepository.findByStatus(Status.PENDING);
        return applicationList.stream()
                .map(application -> new ManageApplicationResponse(application))
                .toList();
    }

    @Transactional
    public void accept(ApplicationAcceptRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("없는 수강신청 입니다.") );
        application.setStatus(Status.ACCEPTED);
    }

    @Transactional
    public void reject(ApplicationAcceptRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow( () -> new NotFoundException("없는 수강신청 입니다.") );
        application.setStatus(Status.REJECTED);
    }
}
