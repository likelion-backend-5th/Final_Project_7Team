package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.PointDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    // 적립금 내역 조회
    @Transactional(readOnly = true)
    public Page<PointDto> getPointList(String loginId, Integer page) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<PointDto> pointListPage = pointRepository.findPointDtoByMemberId(pageable, member.getId());

        return pointListPage;
    }

}
