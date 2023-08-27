package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.mypage.OrderListDto;
import com.likelion.catdogpia.domain.entity.order.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRespository extends JpaRepository<Orders, Long> {

    // 마이페이지 - 주문 내역 조회
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.OrderListDto(o.id, o.orderAt, op.quantity, op.orderStatus, po.size, po.color, p.name, p.price) " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON o.id = op.order.id " +
            "JOIN ProductOption po ON op.id = po.id " +
            "JOIN Product p ON po.id = p.id " +
            "WHERE o.member.id = :memberId")
    Page<OrderListDto> findOrderInfo(Pageable pageable, @Param("memberId") Long memberId);

}
