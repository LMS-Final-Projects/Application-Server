package com.example.lms.application.repository;

import com.example.lms.application.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, String> {
}
