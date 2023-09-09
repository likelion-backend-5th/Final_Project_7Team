package com.likelion.catdogpia.domain.entity.consultation;

public enum ConsulClassification {
    DELIVERY_INQUIRY("배송 문의"),
    ORDER_INQUIRY("주문 문의"),
    EXCHANGE_REQUESTED("교환 신청"),
    REFUND_REQUESTED("환불 신청");

    private final String classification;

    ConsulClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
}
