package com.likelion.catdogpia.domain.entity.mypage;

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

    // 배송지
    @Column(nullable = false, length = 100)
    private String address;

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

    @Builder
    public Address(Long id, String address, Character default_address, String addressName, String name, String phone) {
        this.id = id;
        this.address = address;
        this.defaultAddress = default_address;
        this.addressName = addressName;
        this.name = name;
        this.phone = phone;
    }
}