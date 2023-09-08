package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<QnA, Long> {
}
