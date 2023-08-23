package com.likelion.catdogpia.domain.entity.mypage;

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

    // 배송지 주소
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

    // 배송 요청사항
    @Column(length = 150)
    private String request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Address(Long id, String address, Character defaultAddress, String addressName, String name, String phone, String request, Member member) {
        this.id = id;
        this.address = address;
        this.defaultAddress = defaultAddress;
        this.addressName = addressName;
        this.name = name;
        this.phone = phone;
        this.request = request;
        this.member = member;
    }

    // 기본 배송지 여부 변경 메소드
    public void updateDefaultAddress(Character defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}