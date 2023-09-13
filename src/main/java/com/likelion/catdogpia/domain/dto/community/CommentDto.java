package com.likelion.catdogpia.domain.dto.community;

import com.likelion.catdogpia.domain.dto.admin.ProductOptionDto;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.domain.entity.product.ProductOption;
import com.likelion.catdogpia.domain.entity.user.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    private Member member;
    private String content;
    private LocalDateTime createdAt;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .member(comment.getMember())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Builder
    public CommentDto(Long id, Member member, String content, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.createdAt = createdAt;
    }
}
