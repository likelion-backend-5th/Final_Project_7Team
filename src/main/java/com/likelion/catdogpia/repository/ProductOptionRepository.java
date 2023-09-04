package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
