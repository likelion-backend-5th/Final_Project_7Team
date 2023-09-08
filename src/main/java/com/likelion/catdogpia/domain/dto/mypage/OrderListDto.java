package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
public class OrderListDto {
    
    // * 주문 상품 정보

    // 주문 id (Orders)
    private Long id;

    // 주문 상품 id (OrderProduct)
    private Long opId;

    // 주문번호 (Orders)
    private String orderNumber;

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
    
    // 상품 이미지
    private String fileUrl;

    // (구매확정 상태일 때) 리뷰 작성 여부
    private Boolean writeReview;

    @Builder
    public OrderListDto(Long id, Long opId, String orderNumber, LocalDateTime orderAt, int quantity, OrderStatus orderStatus, String size, String color, String name, int price, int amount, String fileUrl) {
        this.id = id;
        this.opId = opId;
        this.orderNumber = orderNumber;
        this.orderAt = orderAt;
        this.quantity = quantity;
        this.orderStatus = orderStatus.getStatus();
        this.size = size;
        this.color = color;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.fileUrl = fileUrl;
    }

    @Builder
    public OrderListDto(Long id, Long opId, String orderNumber, LocalDateTime orderAt, int quantity, OrderStatus orderStatus, String size, String color, String name, int price, int amount, String fileUrl, boolean writeReview) {
        this.id = id;
        this.opId = opId;
        this.orderNumber = orderNumber;
        this.orderAt = orderAt;
        this.quantity = quantity;
        this.orderStatus = orderStatus.getStatus();
        this.size = size;
        this.color = color;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.fileUrl = fileUrl;
        this.writeReview = writeReview;
    }
}
