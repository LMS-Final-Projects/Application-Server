package com.example.lms.global.kafka;

import com.example.lms.application.entity.*;
import com.example.lms.application.repository.LectureRepository;
import com.example.lms.application.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;

    @KafkaListener(topics = "member", groupId = "application_1")
    public void memberListener(String kafkaMessage) {
        Map<Object, Object> map;
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
            String action = (String) map.get("kafkaAction");
            if (action.equals(KafkaAction.CREATE.name())) {
                Member save = Member.builder()
                        .id((String) map.get("id"))
                        .name((String) map.get("name"))
                        .role((String) map.get("role"))
                        .build();
                memberRepository.save(save);
            }
            System.out.println(map);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    @KafkaListener(topics = "lecture2", groupId = "lecture_2")
    public void lectureListener(String kafkaMessage) {
        Map<Object, Object> map;
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
            String action = (String) map.get("kafkaAction");
            if (action.equals(KafkaAction.CREATE.name())) {
                Object dayOfWeek = map.get("dayOfWeek");
                DayOfWeek dayOfWeekValue = null;

                if (dayOfWeek instanceof String) {
                    String dayOfWeekString = (String) dayOfWeek;
                    dayOfWeekValue = DayOfWeek.valueOf(dayOfWeekString);
                }

                Object semester = map.get("semester");
                Semester semesterValue = null;
                if (semester instanceof String) {
                    String semesterString = (String) semester;
                    semesterValue = Semester.valueOf(semesterString);
                }

                Lecture build = Lecture.builder()
                        .memberId((String) map.get("memberId"))
                        .lectureId((Integer) map.get("lectureId"))
                        .lectureName((String) map.get("lectureName"))
                        .professorName((String) map.get("professorName"))
                        .score((Integer) map.get("score"))
                        .startTime((Integer) map.get("startTime"))
                        .semester(semesterValue)
                        .maximumNumber((Integer) map.get("maximumNumber"))
                        .dayOfWeek(dayOfWeekValue)
                        .year((Integer) map.get("year"))
                        .classTimes((List<Integer>) map.get("classTimes"))
                        .build();

                lectureRepository.save(build);
                System.out.println(map);
            }
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }
}

