package com.example.lms.application.service;

import com.example.lms.application.dto.ApplicationRequest;
import com.example.lms.application.entity.Application;
import com.example.lms.application.repository.ApplicationRepository;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ApplicationServiceTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    @Test
    public void 동시에_수강신청() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 다른 쓰레드의 작업들이 모두 끝날때까지 기다려주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        ApplicationRequest application = ApplicationRequest.builder()
                .lectureId(4L)
                .lectureName("수학")
                .professorName("김봉주")
                .maximumNumber(20L)
                .build();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    applicationService.apply(application);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);
        long count = applicationRepository.count();

        assertThat(count).isEqualTo(20);

    }

    @Test
    public void 동시에_수강신청2() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 다른 쓰레드의 작업들이 모두 끝날때까지 기다려주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        ApplicationRequest application = ApplicationRequest.builder()
                .lectureId(5L)
                .lectureName("수학")
                .professorName("김봉주")
                .maximumNumber(20L)
                .build();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    applicationService.test(application);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(10000);
        long count = applicationRepository.count();

        assertThat(count).isEqualTo(20);

    }

}