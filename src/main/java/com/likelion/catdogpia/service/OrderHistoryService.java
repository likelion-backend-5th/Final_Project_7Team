package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.OrderListDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.repository.OrderRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderRespository orderRespository;
    private final MemberRepository memberRepository;

    // 주문 내역 리스트 조회
    public Page<OrderListDto> readAllOrder(String loginId, Integer page, Integer limit) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<OrderListDto> orderListPage = orderRespository.findOrderInfo(pageable, member.getId());

        return orderListPage;
    }

}
