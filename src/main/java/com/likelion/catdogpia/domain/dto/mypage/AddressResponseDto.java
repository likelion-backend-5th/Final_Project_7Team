package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.mypage.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponseDto {

    // 배송지 주소
    private String address;

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

    public static AddressResponseDto fromEntity(Address entity) {
        return AddressResponseDto.builder()
                .address(entity.getAddress())
                .defaultAddress(entity.getDefaultAddress())
                .addressName(entity.getAddressName())
                .name(entity.getName())
                .phone(entity.getPhone())
                .request(entity.getRequest())
                .build();
    }

}
