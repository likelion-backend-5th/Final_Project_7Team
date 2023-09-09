package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExchangeRefundDto {

    // 주문 상품 id (OrderProduct)
    private Long opId;

    // 상품명 (Product)
    private String pname;

    // 상품 옵션 - size (ProductOption)
    private String size;

    // 상품 옵션 - color (ProductOption)
    private String color;

    // 주문 수량 (OrderProduct)
    private int quantity;

    // 주문 최종 금액 (Orders)
    private int totalAmount;

    // 주문 상태 (OrderProduct)
    private String orderStatus;

    // 회수지 정보 =================================

    // 배송지 주소 (Orders)
    private String address;

    // 수령인 이름 (Orders)
    private String oname;

    // 연락처 (Orders)
    private String phone;

    @Builder
    public ExchangeRefundDto(Long opId, String pname, String size, String color, int quantity, int totalAmount, OrderStatus orderStatus, String address, String oname, String phone) {
        this.opId = opId;
        this.pname = pname;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus.getStatus();
        this.address = address;
        this.oname = oname;
        this.phone = phone;
    }
}
