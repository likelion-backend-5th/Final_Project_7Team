package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.*;
import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefund;
import com.likelion.catdogpia.domain.entity.mypage.Point;
import com.likelion.catdogpia.domain.entity.mypage.PointStatus;
import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ExchangeRefundRepository exchangeRefundRepository;
    private final PointRepository pointRepository;

    // 주문 내역 리스트 조회
    public Page<OrderListDto> getOrderList(String loginId, OrderStatus orderStatus, Integer page) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<OrderListDto> orderListPage = orderRepository.findAllByMemberId(pageable, member.getId(), orderStatus);

        log.info("주문 내역 개수 확인 : " + orderListPage.getTotalElements());

        return orderListPage;
    }

    // 주문 상세 조회 > 특정 주문 번호의 상품들 조회
    public Page<OrderListDto> getOrder(Long orderId, Integer page) {
//        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<OrderListDto> orderListPage = orderRepository.findAllByOrderId(pageable, orderId);

        return orderListPage;
    }

    // 주문 상세 조회 > 배송지 정보, 결제 정보
    public OrderDetailDto getDetail(Long orderId) {
//        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
//        if (optionalMember.isEmpty()) {
//            new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
        Optional<Orders> optionalOrders = orderRepository.findById(orderId);
        if (optionalOrders.isEmpty()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return orderRepository.findDetailByOrderId(orderId);
    }

    // 주문 상태별 개수
    public Map<String, Integer> getOrderCountByStatus(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String, Integer> map = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            int count = orderProductRepository.countOrderStatus(member.getId(), status);
            map.put(String.valueOf(status), count);
        }
        return map;
    }

    // 구매 확정
    @Transactional
    public void purchaseConfirm(Long opId) {

        OrderProduct op = orderProductRepository.findById(opId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!(op.getOrderStatus().equals("배송중") || op.getOrderStatus().equals("배송완료"))) {
            new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 주문 상태 변경
        op.changeOrderStatus(OrderStatus.valueOf("PURCHASE_CONFIRMED"));
        // 구매 확정 일시
        op.changePurchaseConfirmAt();

        // point 적립 (해당 상품 가격의 1%)
        int price = productRepository.findPriceByOrderProduct(opId);

        Orders order = orderRepository.findById(op.getOrder().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long memberId = orderRepository.findMemberIdByOrderProductId(opId);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Point point = Point.builder()
                .status(PointStatus.SAVED)
                .point(price / 100)
                .pointSource("상품 구매 확정")
                .usedAt(LocalDateTime.now())
                .member(member)
                .order(order)
                .build();
        log.info("적립금 : " + point.getPoint());
        pointRepository.save(point);

    }

    // 교환,환불 요청 페이지 - 주문 정보
    public ExchangeRefundDto getOrderInfo(Long opId) {
        return orderProductRepository.findByOrderProductId(opId);
    }

    // 교환 페이지 - 해당 상품 옵션 목록
    public Map<String, List<String>> getProductOption(Long opId) {

        // 해당 상품 조회
        Long pId = productRepository.findProductId(opId);

        List<String> sizeList = productOptionRepository.findSizeByProductIdAndSizeIsNotNull(pId);
        List<String> colorList = productOptionRepository.findSizeByProductIdAndColorIsNotNull(pId);

        // *재고 확인 필요

        Map<String, List<String>> response = new HashMap<>();
        response.put("size", sizeList);
        response.put("color", colorList);

        return response;
    }

    // 교환 요청 처리
    @Transactional
    public void exchange(ExchangeRequestDto dto) {

        // 주문 상품
        OrderProduct op = orderProductRepository.findById(dto.getOpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 주문한 사람과 현재 로그인한 사람이 일치하는지 확인
//        Orders order = orderRepository.findById(op.getOrder().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        if (order.getMember().getLoginId() != loginId) {
//            new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }

        ExchangeRefund exchangeRefund = dto.toEntity(op);

        exchangeRefundRepository.save(exchangeRefund);

        // 주문 상태 변경
        op.changeOrderStatus(OrderStatus.EXCHANGE_REQUESTED);

    }

    // 환불 요청 처리
    @Transactional
    public void refund(RefundRequestDto dto) {

        // 주문 상품
        OrderProduct op = orderProductRepository.findById(dto.getOpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 주문한 사람과 현재 로그인한 사람이 일치하는지 확인
//        Orders order = orderRepository.findById(op.getOrder().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        if (order.getMember().getLoginId() != loginId) {
//            new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }

        ExchangeRefund exchangeRefund = dto.toEntity(op);

        exchangeRefundRepository.save(exchangeRefund);

        // 주문 상태 변경
        op.changeOrderStatus(OrderStatus.REFUND_REQUESTED);

    }

}
