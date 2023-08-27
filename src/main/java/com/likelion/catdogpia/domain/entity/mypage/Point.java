package com.likelion.catdogpia.domain.entity.mypage;

import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.user.Member;
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
    private PointStatus status;
    
    // 적립금
    @Column(nullable = false)
    private int point;
    
    // 적립 내용
    @Column(nullable = false, length = 150)
    private String pointSource;
    
    // 적립 일시
    @Column(nullable = false)
    private LocalDateTime usedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders order;

    @Builder
    public Point(Long id, PointStatus status, int point, String pointSource, LocalDateTime usedAt, Member member, Orders order) {
        this.id = id;
        this.status = status;
        this.point = point;
        this.pointSource = pointSource;
        this.usedAt = usedAt;
        this.member = member;
        this.order = order;
    }
}
