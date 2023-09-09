package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.product.QnAAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnAAnswerRepository extends JpaRepository<QnAAnswer, Long> {
}
