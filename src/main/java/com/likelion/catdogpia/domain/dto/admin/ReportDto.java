package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.report.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReportDto {

    private Long id;
    private String content;
    private String classification;
    private String reporter;
    private String writer;
    private String articleTitle;
    private String articleContent;
    private LocalDateTime reportedAt;
    private LocalDateTime processedAt;

    //== Entity -> DTO ==//
    public static ReportDto fromEntity(Report report) {
        if(report.getComment() != null) {
            return ReportDto.builder()
                    .id(report.getId())
                    .content(report.getContent())
                    .classification("댓글")
                    .reporter(report.getMember().getName())
                    .writer(report.getWriter().getName())
                    .articleContent(report.getComment().getContent())
                    .reportedAt(report.getReportedAt())
                    .processedAt(report.getProcessedAt())
                    .build();
        } else if(report.getArticle() != null) {
            return ReportDto.builder()
                    .id(report.getId())
                    .content(report.getContent())
                    .classification("커뮤니티")
                    .reporter(report.getMember().getName())
                    .writer(report.getWriter().getName())
                    .articleTitle(report.getArticle().getTitle())
                    .articleContent(report.getArticle().getContent())
                    .reportedAt(report.getReportedAt())
                    .processedAt(report.getProcessedAt())
                    .build();
        } else {
            return ReportDto.builder()
                    .id(report.getId())
                    .content(report.getContent())
                    .classification("리뷰")
                    .reporter(report.getMember().getName())
                    .writer(report.getWriter().getName())
                    .articleContent(report.getReview().getDescription())
                    .reportedAt(report.getReportedAt())
                    .processedAt(report.getProcessedAt())
                    .build();
        }
    }

    @Builder
    public ReportDto(Long id, String content, String classification, String reporter, String writer, String articleTitle, String articleContent, LocalDateTime reportedAt, LocalDateTime processedAt) {
        this.id = id;
        this.content = content;
        this.classification = classification;
        this.reporter = reporter;
        this.writer = writer;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.reportedAt = reportedAt;
        this.processedAt = processedAt;
    }
}
