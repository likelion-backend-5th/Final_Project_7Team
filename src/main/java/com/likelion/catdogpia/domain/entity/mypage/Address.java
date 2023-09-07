package com.likelion.catdogpia.domain.entity.mypage;

import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 우편 번호
    @Column(nullable = false, length = 10)
    private String zipCode;

    // 배송지 주소
    @Column(nullable = false, length = 100)
    private String address;

    // 상세 주소
    @Column(length = 50)
    private String detailAddress;

    // 기본 배송지 여부
    @Column(nullable = false, length = 1)
    private Character defaultAddress;

    // 배송지 이름
    @Column(nullable = false, length = 20)
    private String addressName;

    // 수령인 이름
    @Column(nullable = false, length = 20)
    private String name;

    // 수령인 연락처
    @Column(nullable = false, length = 11)
    private String phone;

    // 배송 요청사항
    @Column(length = 150)
    private String request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Address(Long id, String zipCode, String address, String detailAddress, Character defaultAddress, String addressName, String name, String phone, String request, Member member) {
        this.id = id;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.defaultAddress = defaultAddress;
        this.addressName = addressName;
        this.name = name;
        this.phone = phone;
        this.request = request;
        this.member = member;
    }

    public void updateAddress(AddressFormDto dto) {
        this.zipCode = dto.getZipCode();
        this.address = dto.getAddress();
        this.detailAddress = dto.getDetailAddress();
        this.defaultAddress = dto.getDefaultAddress();
        this.addressName = dto.getAddressName();
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.request = dto.getRequest();
    }

    // 기본 배송지 여부 변경
    public void updateDefaultAddress(Character defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}