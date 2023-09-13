package com.likelion.catdogpia.domain.entity.mypage;

import com.likelion.catdogpia.domain.entity.user.Member;
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
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Pet(Long id, String name, String breed, Double weight, Member member) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.member = member;
    }
}