package com.likelion.catdogpia.domain.dto.mypage;

import lombok.*;

import java.time.LocalDateTime;

@Getter
public class OrderListDto {
    
    // * 주문 상품 정보

    // 주문 번호 (Orders)
    private Long id;

    // 주문 일자 (Orders)
    private LocalDateTime orderAt;

    // 주문 수량 (OrderProduct)
    private int quantity;

    // 주문 상태 (OrderProduct)
    private String orderStatus;

    // 상품 옵션 - size (ProductOption)
    private String size;

    // 상품 옵션 - color (ProductOption)
    private String color;

    // 상품명 (Product)
    private String name;

    // 상품가격 (Product)
    private int price;

    // 주문 금액 (quantity * price)
    private int amount;

    @Builder

    public OrderListDto(Long id, LocalDateTime orderAt, int quantity, String orderStatus, String size, String color, String name, int price, int amount) {
        this.id = id;
        this.orderAt = orderAt;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.size = size;
        this.color = color;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }
}
