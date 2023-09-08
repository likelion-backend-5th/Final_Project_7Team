package com.likelion.catdogpia.domain.entity.product;

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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name ="qna_answer")
@SQLDelete(sql ="UPDATE qna_answer SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class QnAAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String answer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private QnA qna;

    // 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //== 답변 변경 메소드 ==//
    public void changeAnswer(String answer, Member member) {
        this.answer = answer;
        this.member = member;
    }

    @Builder
    public QnAAnswer(Long id, String answer, QnA qna, Member member) {
        this.id = id;
        this.answer = answer;
        this.qna = qna;
        this.member = member;
    }
}
