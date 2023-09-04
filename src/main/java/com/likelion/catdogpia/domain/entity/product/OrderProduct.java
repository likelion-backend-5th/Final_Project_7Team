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

    private LocalDateTime deliveryAt;
    private LocalDateTime purchaseConfirmedAt;
    private LocalDateTime exchangeRequestedAt;
    private LocalDateTime exchangeCompletedAt;
    private LocalDateTime refundRequestedAt;
    private LocalDateTime refundCompletedAt;

    @OneToOne(mappedBy = "orderProduct")
    private Review review;


    //== 상태변경 메소드 ==//
    public void changeStatus(String status) {
        LocalDateTime now = LocalDateTime.now();

        this.orderStatus = OrderStatus.valueOf(status);

        switch (status) {
            case "SHIPPED" -> shippedAt = now;
            case "DELIVERED" -> deliveryAt = now;
            case "PURCHASE_CONFIRMED" -> purchaseConfirmedAt = now;
            case "EXCHANGE_REQUESTED" -> exchangeRequestedAt = now;
            case "EXCHANGE_COMPLETED" -> exchangeCompletedAt = now;
            case "REFUND_REQUESTED" -> refundRequestedAt = now;
            case "REFUND_COMPLETED" -> refundCompletedAt = now;
        }
    }

    @Builder
    public OrderProduct(Long id, Orders order, ProductOption productOption, int quantity, OrderStatus orderStatus,
                        LocalDateTime receivedAt, LocalDateTime shippedAt, Review review) {
        this.id = id;
        this.order = order;
        this.productOption = productOption;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.receivedAt = receivedAt;
        this.shippedAt = shippedAt;
        this.review = review;
    }
}
