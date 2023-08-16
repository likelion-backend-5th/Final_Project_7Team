package com.likelion.catdogpia.domain.entity.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name ="review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    // 회원
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    // 주문 상품
//    @OneToOne
//    @JoinColumn(name = "order_product_id")
//    private OrderProduct orderProduct;

    // 파일
//    OneToOne
//    @JoinColumn(name = "attach_id")
//    private Attach attach;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private int rating;










}
