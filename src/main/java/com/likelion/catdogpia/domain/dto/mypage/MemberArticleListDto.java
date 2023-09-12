package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberArticleListDto {

    // 글 id
    private Long articleId;
    
    // 카테고리명
    private String categoryName;

    // 제목
    private String title;

    // 댓글수
    private Long commentCnt;

    // 조회수
    private int viewCnt;
    
    // 좋아요수
    private Long likeCnt;
    
    // 작성일시
    private LocalDateTime createdAt;

    @Builder
    public MemberArticleListDto(Long id, String categoryName, String title, Long commentCnt, int viewCnt, Long likeCnt, LocalDateTime createdAt) {
        this.articleId = id;
        this.categoryName = categoryName;
        this.title = title;
        this.commentCnt = commentCnt;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.createdAt = createdAt;
    }
}
