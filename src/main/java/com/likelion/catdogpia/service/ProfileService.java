package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.MemberModifyFormDto;
import com.likelion.catdogpia.domain.dto.mypage.MemberProfileDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    // 회원 프로필 조회
    public MemberProfileDto getMemberProfile(String loginId) {
        return memberRepository.findProfileDtoByLoginId(loginId);
    }

    // 회원 정보 수정
    @Transactional
    public void updateProfile(String loginId, MemberModifyFormDto dto) {
        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        member.updateMember(dto);
    }

}
