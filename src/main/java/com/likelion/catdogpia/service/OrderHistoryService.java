package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.OrderDetailDto;
import com.likelion.catdogpia.domain.dto.mypage.OrderListDto;
import com.likelion.catdogpia.domain.entity.order.Orders;
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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderRespository orderRespository;
    private final MemberRepository memberRepository;

    // 주문 내역 리스트 조회
    public Page<OrderListDto> readAllOrder(String loginId, String orderStatus, Integer page, Integer limit) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<OrderListDto> orderListPage = orderRespository.findAllByMemberId(pageable, member.getId(), orderStatus);

        return orderListPage;
    }

    // 주문 상세 조회 > 특정 주문 번호의 상품들 조회
    public Page<OrderListDto> readOrder(String loginId, Long orderId, Integer page, Integer limit) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<OrderListDto> orderListPage = orderRespository.findAllByOrderId(pageable, orderId);

        return orderListPage;
    }

    // 주문 상세 조회 > 배송지 정보, 결제 정보
    public OrderDetailDto readDetail(String loginId, Long orderId) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        if(optionalMember.isEmpty()) {
            new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Optional<Orders> optionalOrders = orderRespository.findById(orderId);
        if(optionalOrders.isEmpty()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return orderRespository.findDetailByOrderId(orderId);
    }

}
