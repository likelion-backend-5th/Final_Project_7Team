package com.likelion.catdogpia.domain.entity.consultation;

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
@SQLDelete(sql = "UPDATE consultation_answer SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class ConsultationAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    //== 연관관계 ==/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "consul_id")
    private Consultation consultation;

    //== 답변 변경 메소드 ==//
    public void changeAnswer(String answer, Member member){
        this.answer = answer;
        this.member = member;
    }

    @Builder
    public ConsultationAnswer(Long id, String answer, Member member, Consultation consultation) {
        this.id = id;
        this.answer = answer;
        this.member = member;
        this.consultation = consultation;
    }
}
