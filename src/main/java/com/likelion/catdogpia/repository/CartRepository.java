package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.cart.CartListDto;
import com.likelion.catdogpia.domain.entity.cart.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 장바구니에 담긴 상품 목록
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.cart.CartListDto(c.id, p.name, po.size, po.color, p.price, c.productCnt, p.price * c.productCnt, po.stock) "+
            "FROM Cart c " +
            "JOIN ProductOption po ON c.productOption.id = po.id " +
            "JOIN Product p ON po.product.id = p.id " +
            "WHERE c.member.id = :memberId")
    Page<CartListDto> findAllByMemberId(Pageable pageable, @Param("memberId") Long memberId);

}
