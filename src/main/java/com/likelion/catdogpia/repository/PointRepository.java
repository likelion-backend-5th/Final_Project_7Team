package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.mypage.PointDto;
import com.likelion.catdogpia.domain.entity.mypage.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point, Long> {

    // 포인트 내역 조회
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.PointDto(p.id, p.status, p.point, p.pointSource, p.usedAt, o.orderNumber, SUM(p.point)) " +
            "FROM Point p " +
            "JOIN Orders o ON o.id = p.order.id " +
            "WHERE p.member.id = :memberId " +
            "GROUP BY p.id, p.status, p.pointSource, p.usedAt, o.orderNumber")
    Page<PointDto> findPointDtoByMemberId(Pageable pageable, @Param("memberId") Long memberId);

}
