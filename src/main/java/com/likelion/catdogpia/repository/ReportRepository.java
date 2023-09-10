package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.report.Report;
import com.likelion.catdogpia.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Long countByWriterAndProcessedAtIsNotNull(Member member);
}
