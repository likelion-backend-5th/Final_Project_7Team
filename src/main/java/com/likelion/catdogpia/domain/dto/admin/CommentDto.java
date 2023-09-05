package com.likelion.catdogpia.domain.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String commentWriter;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public CommentDto(Long id, String commentWriter, String content, LocalDateTime createdAt) {
        this.id = id;
        this.commentWriter = commentWriter;
        this.content = content;
        this.createdAt = createdAt;
    }
}
