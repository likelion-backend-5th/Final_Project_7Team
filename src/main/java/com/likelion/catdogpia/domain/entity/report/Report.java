package com.likelion.catdogpia.domain.entity.report;

import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.domain.entity.review.Review;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.domain.entity.user.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime reportedAt;

    private LocalDateTime processedAt;

    //== 연관관계 ==//
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    //== 신고 처리 ==//
    public void processed() {
        this.processedAt = LocalDateTime.now();
    }

    @Builder
    public Report(Long id, String content, LocalDateTime reportedAt, LocalDateTime processedAt, Member member, Member writer, Review review, Comment comment, Article article) {
        this.id = id;
        this.content = content;
        this.reportedAt = reportedAt;
        this.processedAt = processedAt;
        this.member = member;
        this.writer = writer;
        this.review = review;
        this.comment = comment;
        this.article = article;
    }
}
