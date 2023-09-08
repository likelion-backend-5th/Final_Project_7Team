package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.mypage.ExchangeRefundDto;
import com.likelion.catdogpia.domain.dto.mypage.ReviewProductDto;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("SELECT count(op.orderStatus) " +
            "FROM OrderProduct op " +
            "JOIN Orders o ON o.member.id = :memberId " +
            "WHERE op.order.id = o.id " +
            "AND op.orderStatus = :status")
    Integer countOrderStatus(@Param("memberId") Long memberId, @Param("status") OrderStatus status);

    // 마이페이지 - 교환 및 환불 > 주문 정보
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.ExchangeRefundDto(op.id, p.name, po.size, po.color, op.quantity, o.totalAmount, op.orderStatus, o.address, o.name, o.phone) " +
            "FROM OrderProduct op " +
            "JOIN Orders o ON o.id = op.order.id " +
            "JOIN ProductOption po ON po.id = op.productOption.id " +
            "JOIN Product p ON p.id = po.product.id " +
            "WHERE op.id = :opId")
    ExchangeRefundDto findByOrderProductId(@Param("opId") Long opId);

    // 마이페이지 - 리뷰 작성 > 주문 상품 정보
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.ReviewProductDto(op.id, p.name, po.color, po.size) " +
            "FROM OrderProduct op " +
            "JOIN ProductOption po ON po.id = op.productOption.id " +
            "JOIN Product p ON p.id = po.product.id " +
            "WHERE op.id = :opId")
    ReviewProductDto findProductByOrderProductId(@Param("opId") Long opId);

}
