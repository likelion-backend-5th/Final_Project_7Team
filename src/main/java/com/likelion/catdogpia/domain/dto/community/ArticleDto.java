package com.likelion.catdogpia.domain.dto.community;

import com.likelion.catdogpia.domain.dto.admin.AttachDetailDto;
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
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ArticleDto {
    private Long id;
    private Long categoryId; //카테고리
    private Long parentCategoryId; //커뮤니티
    private String title; //제목
    private String content; //내용
    private List<AttachDetailDto> attachDetailList; //이미지
    private Member member; //작성자
    private int viewCnt; //조회수
    private int likeCnt; //좋아요
    private List<Comment> commentList; //댓글
    private LocalDateTime createdAt; //작성일

    //== Entity -> DTO ==//
    public static ArticleDto fromEntity(Article article) {

        List<CommentDto> commentList = new ArrayList<>();
        List<AttachDetailDto> attachDetailList = new ArrayList<>();

        if(article.getAttach() != null) {
            for (AttachDetail attachDetail : article.getAttach().getAttachDetailList()) {
                attachDetailList.add(AttachDetailDto.fromEntity(attachDetail));
            }
        }

        if(article.getCommentList() != null) {
            for (Comment comment : article.getCommentList()) {
                commentList.add(CommentDto.fromEntity(comment));
            }
        }

        return ArticleDto.builder()
                .id(article.getId())
                .categoryId(article.getCategory().getId())
                .parentCategoryId(article.getCategory().getParentCategory().getId())
                .title(article.getTitle())
                .content(article.getContent())
                .attachDetailList(attachDetailList)
                .member(article.getMember())
                .viewCnt(article.getViewCnt())
                .likeCnt(article.getLikeCnt())
                .commentList(article.getCommentList())
                .createdAt(article.getCreatedAt())
                .build();
    }

    @Builder
    public ArticleDto(Long id, Long categoryId, Long parentCategoryId, String title, String content, List<AttachDetailDto> attachDetailList, Member member, int viewCnt, int likeCnt, List<Comment> commentList, LocalDateTime createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.parentCategoryId = parentCategoryId;
        this.title = title;
        this.content = content;
        this.attachDetailList = attachDetailList;
        this.member = member;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.commentList = commentList;
        this.createdAt = createdAt;
    }
}
