package com.likelion.catdogpia.domain.entity.cart;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 개수
    @Column(nullable = false)
    private Integer productCnt;

    @Builder
    public Cart(Long id, Integer productCnt) {
        this.id = id;
        this.productCnt = productCnt;
    }

    // 회원
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;

    // 상품 옵션
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ProductOption productOption;

}
