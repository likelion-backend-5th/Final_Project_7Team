package com.likelion.catdogpia.domain.dto.notice;

import com.likelion.catdogpia.domain.entity.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCnt;

    //== Entity -> DTO //
    public static NoticeDto fromEntity(Notice notice) {
        return NoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .viewCnt(notice.getViewCnt())
                .build();
    }

    @Builder
    public NoticeDto(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCnt = viewCnt;
    }
}
