package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
