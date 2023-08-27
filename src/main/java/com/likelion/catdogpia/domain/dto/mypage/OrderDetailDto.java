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

//    // 상품 가격 합계 (Product)
//    private int totalProdAmount;

    // 주문 총 금액 (Orders)
    private int totalAmount;
    
    // 결제 수단 (Orders)
    private String cardCompany;

    // 할인 합계 (=적립금 사용액) (Point)
    private int point;

    @Builder
    public OrderDetailDto(String address, String name, String phone, String request, int totalAmount, String cardCompany, int point) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.request = request;
        this.totalAmount = totalAmount;
        this.cardCompany = cardCompany;
        this.point = point;
    }
}
