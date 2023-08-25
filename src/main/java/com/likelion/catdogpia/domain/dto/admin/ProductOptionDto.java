package com.likelion.catdogpia.domain.dto.admin;

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

    @Builder
    public ProductOptionDto(Long id, String color, String size, int stock) {
        this.id = id;
        this.color = color;
        this.size = size;
        this.stock = stock;
    }
}
