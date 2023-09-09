package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
