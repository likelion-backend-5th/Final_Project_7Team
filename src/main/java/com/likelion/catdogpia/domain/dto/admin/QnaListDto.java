package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.QnAClassification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class QnaListDto {

    private Long id;
    private QnAClassification classification;
    private String productName;
    private String title;
    private String writer;
    private String answerer;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    @Builder
    public QnaListDto(Long id, QnAClassification classification, String productName, String title, String writer, String answerer, LocalDateTime createdAt, LocalDateTime answeredAt) {
        this.id = id;
        this.classification = classification;
        this.productName = productName;
        this.title = title;
        this.writer = writer;
        this.answerer = answerer;
        this.createdAt = createdAt;
        this.answeredAt = answeredAt;
    }
}
