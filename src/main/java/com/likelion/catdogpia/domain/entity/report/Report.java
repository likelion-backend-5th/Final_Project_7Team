package com.likelion.catdogpia.domain.entity.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime reportedAt;

    private LocalDateTime processedAt;

    @Builder
    public Report(Long id, String content, LocalDateTime reportedAt, LocalDateTime processedAt) {
        this.id = id;
        this.content = content;
        this.reportedAt = reportedAt;
        this.processedAt = processedAt;
    }
}
