package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.community.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommunityDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private int viewCnt;
    private LocalDateTime createdAt;
    private List<String> fileUrlList;

    //== Entity -> dto ==//
    public static CommunityDto fromEntity(Article article) {
        return CommunityDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .writer(article.getMember().getName())
                .content(article.getContent())
                .viewCnt(article.getViewCnt())
                .createdAt(article.getCreatedAt())
                .build();
    }

    @Builder
    public CommunityDto(Long id, String title, String writer, String content, int viewCnt, LocalDateTime createdAt, List<String> fileUrlList) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.viewCnt = viewCnt;
        this.createdAt = createdAt;
        this.fileUrlList = fileUrlList;
    }
}
