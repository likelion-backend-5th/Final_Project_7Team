package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
