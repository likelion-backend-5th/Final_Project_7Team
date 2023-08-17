package com.likelion.catdogpia.domain.entity.product;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name ="product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name ="category_id")
//    private Categoty categoty;

    // 파일
//    @OneToOne
//    @JoinColumn(name = "attach_id")
//    private Attach attach;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 10, nullable = false)
    private String status;

    @Builder
    public Product(Long id, String name, int price, String content, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.content = content;
        this.status = status;
    }
}
