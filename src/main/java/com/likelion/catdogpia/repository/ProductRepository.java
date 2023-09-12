package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.mainpage.HotProductListDto;
import com.likelion.catdogpia.domain.dto.mainpage.NewProductListDto;
import com.likelion.catdogpia.domain.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //대분류 조회 ( 대분류 카테고리 상품 전체 조회 )
    @Query("SELECT p FROM Product p JOIN p.category c ON c.parentCategory.id = :categoryId WHERE p.name LIKE %:searchKeyword%")
    Page<Product> findByCategoryProduct(@Param("categoryId") Long categoryId, Pageable pageable, @Param("searchKeyword") String searchKeyword);

    // 소분류 조회 ( 소분류 카테고리 상품 전체 조회 )
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.name LIKE %:searchKeyword%")
    Page<Product> findBySearchProduct(@Param("categoryId") Long categoryId, Pageable pageable, @Param("searchKeyword") String searchKeyword);

    Page<Product> findAll(Pageable pageable);

    Product findByIdAndName(Long productId, String name);

    Product findByName(String name);

    @Query("SELECT p.id " +
            "FROM Product p " +
            "JOIN OrderProduct op ON op.id = :opId " +
            "JOIN ProductOption po ON po.id = op.productOption.id " +
            "WHERE p.id = po.product.id ")
    Long findProductId(@Param("opId") Long opId);

    // 상품 최신 등록순 조회 (8개)
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mainpage.NewProductListDto(p.id, p.name, p.price, " +
            "COALESCE(ad.fileUrl, 'https://catdogpia.s3.ap-northeast-2.amazonaws.com/product/preparing-product-img.jpg')) " +
            "FROM Product p " +
            "LEFT JOIN AttachDetail ad ON ad.attach.id = p.attach.id " +
            "AND ad.id = (SELECT MIN(ad2.id) FROM AttachDetail ad2 WHERE ad2.attach.id = p.attach.id) " +
            "ORDER BY p.createdAt DESC limit 8")
    List<NewProductListDto> findOrderByCreatedAtDesc();

    // 인기 상품 조회 (최근 주문량순 5개)
    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mainpage.HotProductListDto(p.id, p.name, " +
            "COALESCE(ad.fileUrl, 'https://catdogpia.s3.ap-northeast-2.amazonaws.com/product/preparing-product-img.jpg')) " +
            "FROM Product p " +
            "JOIN ProductOption po ON po.product.id = p.id " +
            "JOIN OrderProduct op ON op.productOption.id = po.id " +
            "JOIN Orders o ON o.id = op.order.id " +
            "LEFT JOIN AttachDetail ad ON ad.attach.id = p.attach.id " +
            "WHERE o.orderAt >= :period " +
            "GROUP BY p, ad.fileUrl " +
            "ORDER BY SUM(op.quantity) DESC limit 5")
    List<HotProductListDto> findTop5BySalesCount(@Param("period") LocalDateTime period); // 기간, 조회개수

    // 주문 상품으로 상품 가격 조회
    @Query("SELECT p.price " +
            "FROM Product p " +
            "LEFT JOIN ProductOption po ON po.product.id = p.id " +
            "LEFT join OrderProduct op ON op.productOption.id = po.id " +
            "WHERE op.id = :opId")
    int findPriceByOrderProduct(@Param("opId") Long opId);


}
