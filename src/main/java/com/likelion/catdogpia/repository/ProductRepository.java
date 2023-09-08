package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
