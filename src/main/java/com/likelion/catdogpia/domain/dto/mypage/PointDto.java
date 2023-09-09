package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.mypage.PointStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class PointDto {

    // id
    private Long id;

    // 상태
    private String status;

    // 적립금
    private int point;

    // 적립 내용
    private String pointSource;

    // 적립 일시
    private LocalDateTime usedAt;

    // 주문 번호 (Orders)
    private String orderNumber;

    // 적립금 총합
    private Long totalPoint;

    @Builder
    public PointDto(Long id, PointStatus status, int point, String pointSource, LocalDateTime usedAt, String orderNumber, Long totalPoint) {
        this.id = id;
        this.status = status.getStatus();
        this.point = point;
        this.pointSource = pointSource;
        this.usedAt = usedAt;
        this.orderNumber = orderNumber;
        this.totalPoint = totalPoint;
    }
}
