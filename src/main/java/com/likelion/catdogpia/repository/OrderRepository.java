package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
