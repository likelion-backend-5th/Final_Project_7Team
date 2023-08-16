package com.likelion.catdogpia.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 개수
    @Column(nullable = false)
    private Integer productCnt;

    // 회원
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;

    // 상품 옵션
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ProductOption productOption;

}
