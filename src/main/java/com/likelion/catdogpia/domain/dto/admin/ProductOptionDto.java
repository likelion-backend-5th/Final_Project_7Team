package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.ProductOption;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ProductOptionDto {

    private Long id;
    private String color;
    private String size;
    private int stock;

    // == Entity -> DTO ==//
    public static ProductOptionDto fromEntity(ProductOption productOption) {
        return ProductOptionDto.builder()
                .id(productOption.getId())
                .color(productOption.getColor())
                .size(productOption.getSize())
                .stock(productOption.getStock())
                .build();
    }

    @Builder
    public ProductOptionDto(Long id, String color, String size, int stock) {
        this.id = id;
        this.color = color;
        this.size = size;
        this.stock = stock;
    }
}
