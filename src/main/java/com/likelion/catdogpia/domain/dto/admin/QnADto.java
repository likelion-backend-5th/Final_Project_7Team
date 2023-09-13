package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.QnA;
import com.likelion.catdogpia.domain.entity.product.QnAClassification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class QnADto {

    private Long id;
    private String productName;
    private String title;
    private String content;
    private String answer;
    private QnAClassification classification;
    private String writer;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    //== Entity -> DTO ==//
    public static QnADto fromEntity(QnA qnA) {

        if(qnA.getQnAAnswer() != null) {
            return QnADto.builder()
                    .id(qnA.getId())
                    .productName(qnA.getProduct().getName())
                    .title(qnA.getTitle())
                    .content(qnA.getContent())
                    .answer(qnA.getQnAAnswer().getAnswer())
                    .classification(qnA.getClassification())
                    .writer(qnA.getMember().getName())
                    .phone(qnA.getMember().getPhone())
                    .createdAt(qnA.getCreatedAt())
                    .answeredAt(qnA.getQnAAnswer().getCreatedAt())
                    .build();
        } else {
            return QnADto.builder()
                    .id(qnA.getId())
                    .productName(qnA.getProduct().getName())
                    .title(qnA.getTitle())
                    .content(qnA.getContent())
                    .classification(qnA.getClassification())
                    .writer(qnA.getMember().getName())
                    .phone(qnA.getMember().getPhone())
                    .createdAt(qnA.getCreatedAt())
                    .build();
        }
    }

    @Builder
    public QnADto(Long id, String productName, String title, String content, String answer, QnAClassification classification, String writer, String phone, LocalDateTime createdAt, LocalDateTime answeredAt) {
        this.id = id;
        this.productName = productName;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.classification = classification;
        this.writer = writer;
        this.phone = phone;
        this.createdAt = createdAt;
        this.answeredAt = answeredAt;
    }
}
