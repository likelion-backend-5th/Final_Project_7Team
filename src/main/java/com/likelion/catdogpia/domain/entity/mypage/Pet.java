package com.likelion.catdogpia.domain.entity.mypage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(nullable = false, length = 20)
    private String name;

    // 견종/묘종
    @Column(nullable = false, length = 20)
    private String breed;

    // 몸무게
    @Column(nullable = false)
    private Integer weight;

    @Builder
    public Pet(Long id, String name, String breed, Integer weight) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
    }

}