package com.likelion.catdogpia.domain.dto.community;

import com.likelion.catdogpia.domain.dto.admin.AttachDetailDto;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.domain.entity.user.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@ToString
public class ArticleListDto {
    private Long id;
    private Long categoryId; //카테고리
    private String title; //제목
    private Member member; //작성자
    private Attach attach; // 이미지
    private int viewCnt; //조회수
    private Long likeCnt; //좋아요
    private Long commentCnt; //댓글수
    private LocalDateTime createdAt; //작성일

    @Builder
    public ArticleListDto(Long id, Long categoryId, String title, Member member, Attach attach, int viewCnt, Long likeCnt, Long commentCnt, LocalDateTime createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.member = member;
        this.attach = attach;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
        this.createdAt = createdAt;
    }
}
