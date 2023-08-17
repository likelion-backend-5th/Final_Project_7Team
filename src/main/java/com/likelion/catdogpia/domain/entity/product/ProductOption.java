package com.likelion.catdogpia.domain.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="product_option")
public class ProductOption {
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
