package com.likelion.catdogpia.domain.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReportListDto {

    private Long id;
    private String content;
    private String classification;
    private String reporter;
    private LocalDateTime reportedAt;
    private LocalDateTime processedAt;

    @Builder
    public ReportListDto(Long id, String content, String classification, String reporter, LocalDateTime reportedAt, LocalDateTime processedAt) {
        this.id = id;
        this.content = content;
        this.classification = classification;
        this.reporter = reporter;
        this.reportedAt = reportedAt;
        this.processedAt = processedAt;
    }
}
