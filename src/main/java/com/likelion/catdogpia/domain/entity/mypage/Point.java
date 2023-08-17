package com.likelion.catdogpia.domain.entity.mypage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상태
    @Column(nullable = false, length = 20)
    private String status;
    
    // 적립금
    @Column(nullable = false)
    private Integer point;
    
    // 적립 내용
    @Column(nullable = false, length = 150)
    private String pointSource;
    
    // 적립 일시
    @Column(nullable = false)
    private LocalDateTime usedAt;

    @Builder
    public Point(Long id, String status, Integer point, String pointSource, LocalDateTime usedAt) {
        this.id = id;
        this.status = status;
        this.point = point;
        this.pointSource = pointSource;
        this.usedAt = usedAt;
    }

}
