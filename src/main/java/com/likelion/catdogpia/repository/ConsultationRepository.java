package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.consultation.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
}
