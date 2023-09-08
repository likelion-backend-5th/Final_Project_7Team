package com.likelion.catdogpia.domain.entity.product;

public enum QnAClassification {
    SIZE("사이즈"),
    DELIVER("배송"),
    RESTOCK("재입고"),
    DETAIL_INQUIRY("상품상세문의");

    private final String classification;

    QnAClassification(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
}
