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
    @Column(name = "consul_id")
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    //== 연관관계 ==/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_consul_id")
    private Consultation parentConsul;

    @OneToMany(mappedBy = "parentConsul", cascade = CascadeType.ALL)
    private List<Consultation> consultationList = new ArrayList<>();

    @Builder
    public Consultation(Long id, String subject, String content, Member member, Consultation parentConsul, List<Consultation> consultationList) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.parentConsul = parentConsul;
        this.consultationList = consultationList;
    }
}
