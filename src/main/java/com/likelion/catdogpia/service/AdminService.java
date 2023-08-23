package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.admin.MemberDto;
import com.likelion.catdogpia.domain.dto.admin.MemberListDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    // 사용자 정보 변경
    @Transactional
    public void modifyMember(MemberDto memberDto, Long memberId) {
        // 관리자인지 확인하는 로직 필요

        // 회원 수정
        Member findMember = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        findMember.changeMember(memberDto);
    }

    // 이메일, 닉네임 사용여부 확인
    public Boolean isDuplicated(String email,String nickname, Long memberId) {
        return memberRepository.findByEmailOrNicknameAndIdNot(email, nickname, memberId).isEmpty();
    }

    // 회원 삭제
    @Transactional
    public void removeMember(Long memberId) {
        // 관리자 인지 확인하는 로직 필요

        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        // 회원 삭제
        memberRepository.delete(findMember);
    }
}
