package com.likelion.catdogpia.domain.dto.notice;

import com.likelion.catdogpia.domain.entity.consultation.ConsulClassification;
import com.likelion.catdogpia.domain.entity.consultation.Consultation;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConsulRequestDto {

    @NotBlank
    private String subject;
    @NotBlank
    private String classification;
    @NotBlank
    private String content;

    @Builder
    public ConsulRequestDto(String subject, String classification, String content) {
        this.subject = subject;
        this.classification = classification;
        this.content = content;
    }
}
