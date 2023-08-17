package com.likelion.catdogpia.domain.entity.notion;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    //private List<Member> memberList = new ArrayList<>();

    @Builder
    public Notion(Long id, String title, String content, int viewCnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCnt = viewCnt;
    }
}
