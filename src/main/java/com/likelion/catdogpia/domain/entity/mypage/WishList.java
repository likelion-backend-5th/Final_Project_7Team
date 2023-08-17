package com.likelion.catdogpia.domain.entity.mypage;

import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Builder
    public WishList(Long id, Member member, Product product) {
        this.id = id;
        this.member = member;
        this.product = product;
    }
}
