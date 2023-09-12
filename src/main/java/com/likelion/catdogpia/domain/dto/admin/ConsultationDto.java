package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.consultation.ConsulClassification;
import com.likelion.catdogpia.domain.entity.consultation.Consultation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ConsultationDto {

    private Long id;
    private ConsulClassification classification;
    private String subject;
    private String content;
    private String writer;
    private String phone;
    private String answerer;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
    private List<String> splitContent;
    private List<String> splitAnswer;

    //== Entity -> DTO ==/
    public static ConsultationDto fromEntity(Consultation consultation) {
        if(consultation.getConsultationAnswer() != null) {
            return ConsultationDto.builder()
                    .id(consultation.getId())
                    .classification(consultation.getClassification())
                    .subject(consultation.getSubject())
                    .content(consultation.getContent())
                    .splitContent((List.of(consultation.getContent().split("\n"))))
                    .writer(consultation.getMember().getName())
                    .phone(consultation.getMember().getPhone())
                    .createdAt(consultation.getCreatedAt())
                    .answerer(consultation.getConsultationAnswer().getMember().getName())
                    .answer(consultation.getConsultationAnswer().getAnswer())
                    .splitAnswer((List.of(consultation.getConsultationAnswer().getAnswer().split("\n"))))
                    .answeredAt(consultation.getConsultationAnswer().getCreatedAt())
                    .build();
        } else {
            return ConsultationDto.builder()
                    .id(consultation.getId())
                    .classification(consultation.getClassification())
                    .subject(consultation.getSubject())
                    .content(consultation.getContent())
                    .splitContent((List.of(consultation.getContent().split("\n"))))
                    .writer(consultation.getMember().getName())
                    .phone(consultation.getMember().getPhone())
                    .createdAt(consultation.getCreatedAt())
                    .build();
        }
    }

    @Builder
    public ConsultationDto(Long id, ConsulClassification classification, String subject, String content, String writer, String phone, String answerer, String answer, LocalDateTime createdAt, LocalDateTime answeredAt, List<String> splitContent, List<String> splitAnswer) {
        this.id = id;
        this.classification = classification;
        this.subject = subject;
        this.content = content;
        this.writer = writer;
        this.phone = phone;
        this.answerer = answerer;
        this.answer = answer;
        this.createdAt = createdAt;
        this.answeredAt = answeredAt;
        this.splitContent = splitContent;
        this.splitAnswer = splitAnswer;
    }
}
