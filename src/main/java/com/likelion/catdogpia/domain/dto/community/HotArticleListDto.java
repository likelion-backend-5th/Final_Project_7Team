package com.likelion.catdogpia.domain.dto.community;

import com.likelion.catdogpia.domain.dto.admin.AttachDetailDto;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class HotArticleListDto {

    private Long id;
    private String title; //제목
    private Attach attach; // 파일명
    private Long likeCnt; //좋아요
    private LocalDateTime createdAt; //작성일


    @Builder
    public HotArticleListDto(Long id, String title, Attach attach, Long likeCnt, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.attach = attach;
        this.likeCnt = likeCnt;
        this.createdAt = createdAt;
    }
}
