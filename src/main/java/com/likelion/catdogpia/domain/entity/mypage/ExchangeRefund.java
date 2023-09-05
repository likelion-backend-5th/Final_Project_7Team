package com.likelion.catdogpia.domain.entity.mypage;

import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRefund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 교환 or 환불 구분
    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    private ExchangeRefundType exchangeOrRefund;

    // 사유
    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false)
    private ExchangeRefundReason reason;

    // 상세 사유
    @Column(columnDefinition = "TEXT", nullable = false)
    private String detailReason;

//    // 쇼핑몰 측 책임 여부
//    @Column(length = 1, nullable = false)
//    private Character isSellerResponsible;

    // (교환시) 선택 옵션 - size
    @Column(length = 30)
    private String size;

    // (교환시) 선택 옵션 - color
    @Column(length = 20)
    private String color;

    // 승인 여부
    @Column(length = 1)
    private Character isApproval;

    // 총 환불 금액
    private Integer totalRefundAmount;

    // 교환 요청 일시
    private LocalDateTime exchangeRequestedAt;

    // 교환 완료 일시
    private LocalDateTime exchangeCompletedAt;

    // 환불 요청 일시
    private LocalDateTime refundRequestedAt;

    // 환불 완료 일시
    private LocalDateTime refundCompletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @Builder
    public ExchangeRefund(Long id, ExchangeRefundType exchangeOrRefund, ExchangeRefundReason reason, String detailReason, String size, String color, Character isApproval, Integer totalRefundAmount, LocalDateTime exchangeRequestedAt, LocalDateTime exchangeCompletedAt, LocalDateTime refundRequestedAt, LocalDateTime refundCompletedAt, OrderProduct orderProduct) {
        this.id = id;
        this.exchangeOrRefund = exchangeOrRefund;
        this.reason = reason;
        this.detailReason = detailReason;
        this.size = size;
        this.color = color;
        this.isApproval = isApproval;
        this.totalRefundAmount = totalRefundAmount;
        this.exchangeRequestedAt = exchangeRequestedAt;
        this.exchangeCompletedAt = exchangeCompletedAt;
        this.refundRequestedAt = refundRequestedAt;
        this.refundCompletedAt = refundCompletedAt;
        this.orderProduct = orderProduct;
    }
}
