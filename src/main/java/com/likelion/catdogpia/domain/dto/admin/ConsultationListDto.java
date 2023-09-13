package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.consultation.ConsulClassification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ConsultationListDto {

    private Long id;
    private ConsulClassification classification;
    private String subject;
    private String writer;
    private String answerer;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    @Builder
    public ConsultationListDto(Long id, ConsulClassification classification, String subject, String writer, String answerer, LocalDateTime createdAt, LocalDateTime answeredAt) {
        this.id = id;
        this.classification = classification;
        this.subject = subject;
        this.writer = writer;
        this.answerer = answerer;
        this.createdAt = createdAt;
        this.answeredAt = answeredAt;
    }
}
