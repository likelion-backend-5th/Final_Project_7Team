package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.id " +
            "FROM Product p " +
            "JOIN OrderProduct op ON op.id = :opId " +
            "JOIN ProductOption po ON po.id = op.productOption.id " +
            "WHERE p.id = po.product.id ")
    Long findProductId(@Param("opId") Long opId);

}
