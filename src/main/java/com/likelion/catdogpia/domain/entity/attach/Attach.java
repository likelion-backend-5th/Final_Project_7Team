package com.likelion.catdogpia.domain.entity.attach;

import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Attach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //파일상세 연관관계
    @OneToMany(mappedBy = "attach")
    private List<AttachDetail> attachDetailList = new ArrayList<>();

    //상품 연관관계
    @OneToOne(mappedBy = "attach")
    private Product product;

    //커뮤니티 글 연관관계
    @OneToOne(mappedBy = "attach")
    private Article article;

    //리뷰 연관관계
    @OneToOne(mappedBy = "attach")
    private Review review;

    @Builder
    public Attach(Long id, LocalDateTime createdAt, List<AttachDetail> attachDetailList, Product product, Article article, Review review) {
        this.id = id;
        this.createdAt = createdAt;
        this.attachDetailList = attachDetailList;
        this.product = product;
        this.article = article;
        this.review = review;
    }
}