package com.likelion.catdogpia.domain.entity.cart;

import com.likelion.catdogpia.domain.entity.product.ProductOption;
import com.likelion.catdogpia.domain.entity.user.Member;
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
    private int productCnt;

    // 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    // 상품 옵션
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_option_id")
    private ProductOption productOption;

    @Builder
    public Cart(Long id, int productCnt, Member member, ProductOption productOption) {
        this.id = id;
        this.productCnt = productCnt;
        this.member = member;
        this.productOption = productOption;
    }
}
