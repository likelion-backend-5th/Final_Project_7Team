package com.likelion.catdogpia.domain.entity.review;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name ="review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    //  회원
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

    @Builder
    public Review(Long id, String description, int rating) {
        this.id = id;
        this.description = description;
        this.rating = rating;
    }
}
