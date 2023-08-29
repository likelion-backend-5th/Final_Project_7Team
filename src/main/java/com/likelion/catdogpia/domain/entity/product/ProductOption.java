package com.likelion.catdogpia.domain.entity.product;

import com.likelion.catdogpia.domain.dto.admin.ProductOptionDto;
import com.likelion.catdogpia.domain.entity.BaseEntity;
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
@Table(name ="product_option")
@SQLDelete(sql ="UPDATE product_option SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class ProductOption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="product_id")
    private Product product;

    @Column(length = 30)
    private String size;

    @Column(length = 20)
    private String color;

    private int stock;

    @OneToMany(mappedBy = "productOption")
    private List<OrderProduct> orderProductList = new ArrayList<>();

    //== 상품 옵션 수정 메소드 ==//
    public void changeProductOption(ProductOptionDto productOptionDto, Product product){
        this.id = productOptionDto.getId();
        this.color = productOptionDto.getColor();
        this.size = productOptionDto.getSize();
        this.stock = productOptionDto.getStock();
        this.product = product;
    }

    @Builder
    public ProductOption(Long id, Product product, String size, String color, int stock, List<OrderProduct> orderProductList) {
        this.id = id;
        this.product = product;
        this.size = size;
        this.color = color;
        this.stock = stock;
        this.orderProductList = orderProductList;
    }
}
