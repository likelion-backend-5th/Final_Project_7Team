package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDetailDto {

    // * 배송지 정보, 결제 정보

    // 배송지 주소 (Orders)
    private String address;
    
    // 수령인 이름 (Orders)
    private String name;
    
    // 연락처 (Orders)
    private String phone;
    
    // 요청사항 (Orders)
    private String request;

    // 상품 가격 합계 (Product)
//    private int totalProdAmount;

    // 배송비
    private int deliveryCharge;

    // 할인 합계 (=적립금 사용액) (Orders)
    private int discountAmount;

    // 주문 최종 금액 (Orders)
    private int totalAmount;
    
    // 결제 수단 (Orders)
    private String cardCompany;

    @Builder
    public OrderDetailDto(String address, String name, String phone, String request, int deliveryCharge, int discountAmount, int totalAmount, String cardCompany) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.request = request;
        this.deliveryCharge = deliveryCharge;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.cardCompany = cardCompany;
    }
}
