package com.likelion.catdogpia.domain.entity.consultation;

public enum ConsulClassification {
    DELIVERY_INQUIRY("배송문의"),
    ORDER_INQUIRY("주문문의"),
    EXCHANGE_REQUESTED("교환신청"),
    REFUND_REQUESTED("환불신청"),
    ETC("기타문의");

    private final String classification;

    ConsulClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
}
