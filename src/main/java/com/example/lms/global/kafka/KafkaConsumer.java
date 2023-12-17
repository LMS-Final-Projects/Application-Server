package com.example.lms.global.kafka;

import com.example.lms.application.entity.Member;
import com.example.lms.application.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MemberRepository memberRepository;

    @KafkaListener(topics = "member", groupId = "group_1")
    public void listener(String kafkaMessage) {
        Map<Object, Object> map;
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
            String action = (String) map.get("kafkaAction");
            if (action.equals(KafkaAction.CREATE.name())) {
                System.out.println("무야호");
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
}

