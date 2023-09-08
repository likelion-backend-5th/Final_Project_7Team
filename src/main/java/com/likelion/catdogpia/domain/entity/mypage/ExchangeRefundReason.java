package com.likelion.catdogpia.domain.entity.mypage;

public enum ExchangeRefundReason {
    BUYER_SIMPLE_REMORSE("단순 변심", false),
    BUYER_WRONG_OPTION("잘못된 옵션 선택", false),
    BUYER_WRONG_SIZE("사이즈가 맞지 않음", false),
    BUYER_OTHER("기타", false),
    SELLER_DEFECTIVE_PRODUCT("상품 불량", true),
    SELLER_WRONG_PRODUCT_INFO("상품 등록 정보 오류", true),
    SELLER_WRONG_PRODUCT_DELIVERY("주문과 다른 상품 배송", true),
    SELLER_OTHER("기타", true);

    private final String reason;

    // 쇼핑몰 측 책임 사유인지 구분
    private final boolean isSellerResponsible;

    ExchangeRefundReason(String reason, boolean isSellerResponsible) {
        this.reason = reason;
        this.isSellerResponsible = isSellerResponsible;
    }

    public String getReason() {
        return reason;
    }

    public boolean isSellerResponsible() {
        return isSellerResponsible;
    }
}
