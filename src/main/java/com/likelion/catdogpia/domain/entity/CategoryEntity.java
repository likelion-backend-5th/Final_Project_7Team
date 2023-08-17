package com.likelion.catdogpia.domain.entity;

import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 1, nullable = false)
    private Character useYn;

    //카테고리(대분류-중분류) 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private CategoryEntity parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<CategoryEntity> categoryEntityList = new ArrayList<>();

    //상품 연관관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

    //커뮤니티 연관관계
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Article> articleList = new ArrayList<>();

    @Builder
    public CategoryEntity(Long id, String name, Character useYn) {
        this.id = id;
        this.name = name;
        this.useYn = useYn;
    }
}
