package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickname);

    Boolean existsByEmail(String email);

    Optional<Member> findByNameAndEmail(String name, String email);
}
