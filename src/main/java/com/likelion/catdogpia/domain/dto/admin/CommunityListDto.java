package com.likelion.catdogpia.domain.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityListDto {

    private Long id;
    private String title;
    private String writer;
    private int likeCnt;
    private int viewCnt;
    private int reportCnt;

    @Builder
    public CommunityListDto(Long id, String title, String writer, int likeCnt, int viewCnt, int reportCnt) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.likeCnt = likeCnt;
        this.viewCnt = viewCnt;
        this.reportCnt = reportCnt;
    }
}
