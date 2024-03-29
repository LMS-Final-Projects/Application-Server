package com.example.lms.application.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CheckRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long increment(Integer lectureId) {
        return redisTemplate
                .opsForValue()
                .increment(String.valueOf(lectureId));
    }

    public Long decrement(Integer lectureId) {
        return redisTemplate
                .opsForValue()
                .decrement(String.valueOf(lectureId));
    }
}
