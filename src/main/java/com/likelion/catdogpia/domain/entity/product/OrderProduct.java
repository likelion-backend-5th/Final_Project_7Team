package com.likelion.catdogpia.domain.entity.product;

import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.review.Review;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    // 상품 옵션
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private ProductOption productOption;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime receivedAt;

    private LocalDateTime shippedAt;

    @OneToOne(mappedBy = "orderProduct")
    private Review review;

    // 배송 완료 일시
    private LocalDateTime deliveryAt;

    // 구매 확정 일시
    private LocalDateTime purchaseConfirmedAt;

    // 교환 요청 일시
    private LocalDateTime exchangeRequestedAt;

    // 교환 완료 일시
    private LocalDateTime exchangeCompletedAt;

    // 환불 요청 일시
    private LocalDateTime refundRequestedAt;

    // 환불 완료 일시
    private LocalDateTime refundCompletedAt;

    @Builder
    public OrderProduct(Long id, Orders order, ProductOption productOption, int quantity, OrderStatus orderStatus, LocalDateTime receivedAt, LocalDateTime shippedAt, Review review, LocalDateTime deliveryAt, LocalDateTime purchaseConfirmedAt, LocalDateTime exchangeRequestedAt, LocalDateTime exchangeCompletedAt, LocalDateTime refundRequestedAt, LocalDateTime refundCompletedAt) {
        this.id = id;
        this.order = order;
        this.productOption = productOption;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.receivedAt = receivedAt;
        this.shippedAt = shippedAt;
        this.review = review;
        this.deliveryAt = deliveryAt;
        this.purchaseConfirmedAt = purchaseConfirmedAt;
        this.exchangeRequestedAt = exchangeRequestedAt;
        this.exchangeCompletedAt = exchangeCompletedAt;
        this.refundRequestedAt = refundRequestedAt;
        this.refundCompletedAt = refundCompletedAt;
    }
}
