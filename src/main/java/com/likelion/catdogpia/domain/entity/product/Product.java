package com.likelion.catdogpia.domain.entity.product;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import com.likelion.catdogpia.domain.entity.CategoryEntity;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name ="product")
@SQLDelete(sql ="UPDATE product SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id")
    private CategoryEntity category;

    // 파일
    @OneToOne
    @JoinColumn(name = "attach_id")
    private Attach attach;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(length = 10, nullable = false)
    private String status;

    @OneToMany(mappedBy = "product")
    private List<ProductOption> productOptionList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<QnA> qnAList = new ArrayList<>();

    @Builder
    public Product(Long id, CategoryEntity category, Attach attach, String name,
                   int price, String status, List<ProductOption> productOptionList, List<QnA> qnAList) {
        this.id = id;
        this.category = category;
        this.attach = attach;
        this.name = name;
        this.price = price;
        this.status = status;
        this.productOptionList = productOptionList;
        this.qnAList = qnAList;
    }
}
