package com.likelion.catdogpia.domain.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductOptionDto {
    // 재고
    private int stock;

    // 색상
    private String color;

    // 사이즈
    private String size;
}
