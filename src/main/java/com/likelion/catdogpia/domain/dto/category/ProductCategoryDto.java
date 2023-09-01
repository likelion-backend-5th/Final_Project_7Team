package com.likelion.catdogpia.domain.dto.category;

import com.likelion.catdogpia.domain.entity.CategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCategoryDto {

    private Long id;

    private Long parentId;

    private String parentName;

    private String name;

    public static ProductCategoryDto fromEntity(CategoryEntity category) {
        return ProductCategoryDto.builder()
                .id(category.getId())
                .parentId(category.getParentCategory().getId())
                .parentName(category.getParentCategory().getName())
                .name(category.getName())
                .build();
    }
}
