package com.likelion.catdogpia.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(name = "use_yn", nullable = false)
    private String useYn;

    @Builder
    public CategoryEntity(Long categoryId, String name, String useYn) {
        this.categoryId = categoryId;
        this.name = name;
        this.useYn = useYn;
    }
}
