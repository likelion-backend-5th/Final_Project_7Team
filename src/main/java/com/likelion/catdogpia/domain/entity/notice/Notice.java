package com.likelion.catdogpia.domain.entity.notice;

import com.likelion.catdogpia.domain.dto.notice.NoticeDto;
import com.likelion.catdogpia.domain.entity.BaseEntity;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
@Entity
@SQLDelete(sql = "UPDATE notice SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "0")
    private int viewCnt;

    //== 연관관계 ==//
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //== DTO -> Entity ==/
    public static Notice toEntity(NoticeDto noticeDto, Member member) {
        return Notice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .member(member)
                .build();
    }

    //== 게시글 수정 ==//
    public void changeNotice(NoticeDto noticeDto, Member member) {
        this.title = noticeDto.getTitle();
        this.content = noticeDto.getContent();
        this.member = member;
    }

    @Builder
    public Notice(Long id, String title, String content, int viewCnt, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCnt = viewCnt;
        this.member = member;
    }
}
