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
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.cart.CartListDto(c.id, p.name, po.size, po.color, p.price, c.productCnt, p.price * c.productCnt, po.stock, ad.fileUrl) " +
            "FROM Cart c " +
            "JOIN ProductOption po ON c.productOption.id = po.id " +
            "JOIN Product p ON po.product.id = p.id " +
            "LEFT JOIN AttachDetail ad ON ad.attach.id = p.attach.id " +
            "WHERE c.member.id = :memberId " +
            "AND ad.id = (SELECT MIN(ad2.id) FROM AttachDetail ad2 WHERE ad2.attach.id = p.attach.id)")
    Page<CartListDto> findAllByMemberId(Pageable pageable, @Param("memberId") Long memberId);

}
