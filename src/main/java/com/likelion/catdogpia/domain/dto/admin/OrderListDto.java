package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderListDto {

    private Long id;
    private Long orderProductId;
    private String orderNumber;
    private String productName;
    private String buyerName;
    private String color;
    private String size;
    private OrderStatus orderStatus;
    private LocalDateTime orderAt;
    private int quantity;

    @Builder
    public OrderListDto(Long id, Long orderProductId, String orderNumber, String productName, String buyerName, String color, String size, OrderStatus orderStatus, LocalDateTime orderAt, int quantity) {
        this.id = id;
        this.orderProductId = orderProductId;
        this.orderNumber = orderNumber;
        this.productName = productName;
        this.buyerName = buyerName;
        this.color = color;
        this.size = size;
        this.orderStatus = orderStatus;
        this.orderAt = orderAt;
        this.quantity = quantity;
    }
}
