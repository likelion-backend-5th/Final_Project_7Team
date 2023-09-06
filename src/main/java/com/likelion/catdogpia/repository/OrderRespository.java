package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.mypage.OrderDetailDto;
import com.likelion.catdogpia.domain.dto.mypage.OrderListDto;
import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRespository extends JpaRepository<Orders, Long> {

    // 마이페이지 - 주문 내역 조회
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.OrderListDto(o.id, op.id, o.orderNumber, o.orderAt, op.quantity, op.orderStatus, po.size, po.color, p.name, p.price, op.quantity * p.price) " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON o.id = op.order.id " +
            "JOIN ProductOption po ON op.productOption.id = po.id " +
            "JOIN Product p ON po.product.id = p.id " +
            "WHERE o.member.id = :memberId AND (:orderStatus is null or op.orderStatus = :orderStatus)")
    Page<OrderListDto> findAllByMemberId(Pageable pageable, @Param("memberId") Long memberId, @Param("orderStatus") OrderStatus orderStatus);

    // 마이페이지 - 주문 상세 조회 > 특정 주문 번호의 상품들 조회
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.OrderListDto(o.id, op.id, o.orderNumber, o.orderAt, op.quantity, op.orderStatus, po.size, po.color, p.name, p.price, op.quantity * p.price) " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON o.id = op.order.id " +
            "JOIN ProductOption po ON op.productOption.id = po.id " +
            "JOIN Product p ON po.product.id = p.id " +
            "WHERE o.id = :orderId")
    Page<OrderListDto> findAllByOrderId(Pageable pageable, @Param("orderId") Long orderId);

    // 마이페이지 - 주문 상세 조회 > 배송지 정보, 결제 정보
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.OrderDetailDto(o.address, o.name, o.phone, o.request, o.deliveryCharge, o.discountAmount, o.totalAmount, o.cardCompany ) " +
            "FROM Orders o " +
//            "LEFT JOIN Point p ON o.id = p.order.id AND p.status = com.likelion.catdogpia.domain.entity.mypage.PointStatus.SAVED " +
            "WHERE o.id = :orderId")
    OrderDetailDto findDetailByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT o.member.id " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON op.id = :opId " +
            "WHERE o.id = op.order.id")
    Long findMemberIdByOrderProductId(@Param("opId") Long opId);

}
