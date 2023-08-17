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
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 1, nullable = false)
    private Character useYn;

    @Builder
    public CategoryEntity(Long id, String name, Character useYn) {
        this.id = id;
        this.name = name;
        this.useYn = useYn;
    }
}
