package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.admin.MemberDto;
import com.likelion.catdogpia.domain.dto.admin.MemberListDto;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;
    private final QueryRepository queryRepository;

    // 회원관리 목록
    public Page<MemberListDto> findMemberList(Pageable pageable, String filter, String keyword) {
        // 사용자가 관리자인지 확인하는 로직 필요

        // 목록 리턴
        return queryRepository.findByFilterAndKeyword(pageable, filter, keyword);
    }

    // 사용자 상세 조회
    public MemberDto findMember(Long memberId) {
        // 관리자인지 확인하는 로직 필요

        return memberRepository.findByMember(memberId);
    }
}
