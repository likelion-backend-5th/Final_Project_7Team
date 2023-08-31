package com.likelion.catdogpia.repository;

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

}
