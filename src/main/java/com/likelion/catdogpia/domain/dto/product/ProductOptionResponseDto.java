package com.likelion.catdogpia.domain.dto.product;


import com.likelion.catdogpia.domain.entity.product.ProductOption;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductOptionResponseDto {

    private Long id;

    private String size;

    private String color;

    private int stock;

    public static ProductOptionResponseDto fromEntity(ProductOption option){
        return ProductOptionResponseDto.builder()
                .id(option.getId())
                .size(option.getSize())
                .color(option.getColor())
                .stock(option.getStock())
                .build();
    }
}
