package com.likelion.catdogpia.domain.entity.product;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name ="qna")
public class QnA extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Self -Reference
    private Long ParentQnaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_qna_id")
    private QnA parent;

    @OneToMany(mappedBy = "parent")
    private List<QnA> children = new ArrayList<>();

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    public QnA(Long id, Long parentQnaId, QnA parent, List<QnA> children, Product product, Member member, String title, String content) {
        this.id = id;
        ParentQnaId = parentQnaId;
        this.parent = parent;
        this.children = children;
        this.product = product;
        this.member = member;
        this.title = title;
        this.content = content;
    }
}
