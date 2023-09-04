package com.likelion.catdogpia.domain.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductListDto {

    private Long id;
    private String name;
    private String status;
    private int price;
    private int totalStock;

    @Builder
    public ProductListDto(Long id, String name, String status, int price, int totalStock) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.price = price;
        this.totalStock = totalStock;
    }
}
