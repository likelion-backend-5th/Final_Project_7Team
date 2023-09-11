package com.likelion.catdogpia.domain.entity.consultation;

import com.likelion.catdogpia.domain.dto.notice.ConsulRequestDto;
import com.likelion.catdogpia.domain.entity.BaseEntity;
import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Getter
@Entity
@SQLDelete(sql = "UPDATE consultation SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Consultation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsulClassification classification;

    //== 연관관계 ==/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "consultation")
    private ConsultationAnswer consultationAnswer;

    //== DTO -> Entity ==//
    public static Consultation toEntity(ConsulRequestDto dto, Member member){

        return Consultation.builder()
                .subject(dto.getSubject())
                .classification(ConsulClassification.valueOf(dto.getClassification()))
                .content(dto.getContent())
                .member(member)
                .build();
    }

    @Builder
    public Consultation(Long id, String subject, String content, ConsulClassification classification, Member member, ConsultationAnswer consultationAnswer) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.classification = classification;
        this.member = member;
        this.consultationAnswer = consultationAnswer;
    }
}
