package com.likelion.catdogpia.domain.entity.review;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name ="review")
@SQLDelete(sql ="UPDATE review SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 주문 상품
    @OneToOne
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    // 파일
    @OneToOne
    @JoinColumn(name = "attach_id")
    private Attach attach;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private int rating;

    @Builder
    public Review(Long id, Member member, OrderProduct orderProduct, Attach attach, String description, int rating) {
        this.id = id;
        this.member = member;
        this.orderProduct = orderProduct;
        this.attach = attach;
        this.description = description;
        this.rating = rating;
    }
}
