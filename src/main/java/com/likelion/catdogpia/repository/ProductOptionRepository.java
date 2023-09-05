package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    // 특정 상품의 옵션 목록 조회 (size)
    @Query("SELECT po.size " +
            "FROM ProductOption po " +
            "WHERE po.product.id = :pId AND po.size IS NOT NULL")
    List<String> findSizeByProductIdAndSizeIsNotNull(@Param("pId") Long pId);

    // 특정 상품의 옵션 목록 조회 (color)
    @Query("SELECT po.color " +
            "FROM ProductOption po " +
            "WHERE po.product.id = :pId AND po.color IS NOT NULL")
    List<String> findSizeByProductIdAndColorIsNotNull(@Param("pId") Long pId);

}
