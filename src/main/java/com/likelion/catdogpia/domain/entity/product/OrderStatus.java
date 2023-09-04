package com.likelion.catdogpia.domain.entity.product;

public enum OrderStatus {
    RECEIVED("주문접수"),
    PREPARING_FOR_SHIPMENT("배송준비"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    PURCHASE_CONFIRMED("구매확정"),
    EXCHANGE_REQUESTED("교환요청"),
    EXCHANGE_COMPLETED("교환완료"),
    REFUND_REQUESTED("환불요청"),
    REFUND_COMPLETED("환불완료");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}






