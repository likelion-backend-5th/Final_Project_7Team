package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.mypage.Address;
import com.likelion.catdogpia.domain.entity.user.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressFormDto {

    // 우편 번호
    private String zipCode;

    // 배송지 주소
    private String address;
    
    // 배송지 상세 주소
    private String detailAddress;

    // 기본 배송지 여부
    private Character defaultAddress;

    // 배송지 이름
    private String addressName;

    // 수령인 이름
    private String name;

    // 수령인 연락처
    private String phone;

    // 배송 요청사항
    private String request;

    public Address toEntity(Member member) {
        return Address.builder()
                .zipCode(zipCode)
                .address(address)
                .detailAddress(detailAddress)
                .defaultAddress(defaultAddress)
                .addressName(addressName)
                .name(name)
                .phone(phone)
                .request(request)
                .member(member)
                .build();
    }

}
