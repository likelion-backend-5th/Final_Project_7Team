package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
