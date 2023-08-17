package com.likelion.catdogpia.domain.entity.notion;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
@Entity
public class Notion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private int viewCnt;

    private int likeCnt;

    private LocalDateTime deletedAt;

    //private List<Member> memberList = new ArrayList<>();

    @Builder
    public Notion(Long id, String title, String content, int viewCnt, int likeCnt, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.deletedAt = deletedAt;
    }
}
