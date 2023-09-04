package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.attach.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachRepository extends JpaRepository<Attach, Long> {
}
