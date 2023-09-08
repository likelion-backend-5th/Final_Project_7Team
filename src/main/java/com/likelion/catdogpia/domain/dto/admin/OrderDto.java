package com.likelion.catdogpia.domain.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class OrderDto {

    private Long id;
    private String orderNumber;
    private String buyerName;
    private String address;
    private String email;
    private String phone;
    private String request;
    private String cardCompany;
    private LocalDateTime orderAt;
    private List<OrderProductDto> orderProductList = new ArrayList<>();
    private int totalAmount;
    private int deliveryCharge;
    private int discountAmount;


    @Builder
    public OrderDto(Long id, String orderNumber, String buyerName, String address, String email, String phone, String request, String cardCompany, LocalDateTime orderAt, List<OrderProductDto> orderProductList, int totalAmount, int deliveryCharge, int discountAmount) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.buyerName = buyerName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.request = request;
        this.cardCompany = cardCompany;
        this.orderAt = orderAt;
        this.orderProductList = orderProductList;
        this.totalAmount = totalAmount;
        this.deliveryCharge = deliveryCharge;
        this.discountAmount = discountAmount;
    }
}
