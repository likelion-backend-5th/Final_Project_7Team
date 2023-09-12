package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductListDto {

    private Long id;
    private String name;
    private ProductStatus status;
    private int price;
    private int totalStock;

    @Builder
    public ProductListDto(Long id, String name, ProductStatus status, int price, int totalStock) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.price = price;
        this.totalStock = totalStock;
    }
}
