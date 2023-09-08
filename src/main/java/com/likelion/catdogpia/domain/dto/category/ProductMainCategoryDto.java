package com.likelion.catdogpia.domain.dto.category;

import com.likelion.catdogpia.domain.entity.CategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Getter

@Builder
public class ProductMainCategoryDto {
    private Long id;

    private String name;

    public static ProductMainCategoryDto fromEntity(CategoryEntity category){
        return ProductMainCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
