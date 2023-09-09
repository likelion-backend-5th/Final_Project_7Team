package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class OrderProductDto {

    private Long id;
    private String productName;
    private String color;
    private String size;
    private String imgUrl;
    private OrderStatus orderStatus;
    // 이 부분은 변경 필요
    private LocalDateTime deliveryAt;
    private LocalDateTime purchaseConfirmedAt;
    private LocalDateTime exchangeRequestedAt;
    private LocalDateTime exchangeCompletedAt;
    private LocalDateTime refundRequestedAt;
    private LocalDateTime refundCompletedAt;
    private int quantity;
    private int price;


    @Builder
    public OrderProductDto(Long id, String productName, String color, String size, String imgUrl, OrderStatus orderStatus, LocalDateTime deliveryAt, LocalDateTime purchaseConfirmedAt, LocalDateTime exchangeRequestedAt, LocalDateTime exchangeCompletedAt, LocalDateTime refundRequestedAt, LocalDateTime refundCompletedAt, int quantity, int price) {
        this.id = id;
        this.productName = productName;
        this.color = color;
        this.size = size;
        this.imgUrl = imgUrl;
        this.orderStatus = orderStatus;
        this.deliveryAt = deliveryAt;
        this.purchaseConfirmedAt = purchaseConfirmedAt;
        this.exchangeRequestedAt = exchangeRequestedAt;
        this.exchangeCompletedAt = exchangeCompletedAt;
        this.refundRequestedAt = refundRequestedAt;
        this.refundCompletedAt = refundCompletedAt;
        this.quantity = quantity;
        this.price = price;
    }
}
