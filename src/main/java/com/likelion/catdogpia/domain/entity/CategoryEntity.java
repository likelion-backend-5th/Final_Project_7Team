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

    @Column(length = 30, nullable = false)
    private String name;

    @Column(name = "use_yn", length = 1,nullable = false)
    private Character useYn;

    @Builder
    public CategoryEntity(Long categoryId, String name, Character useYn) {
        this.categoryId = categoryId;
        this.name = name;
        this.useYn = useYn;
    }
}
