package com.likelion.catdogpia.domain.dto.product;

import com.likelion.catdogpia.domain.entity.CategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCategoryListDto {

    private Long id;

    private Long parentId;

    private String parentName;

    private String name;

    public static ProductCategoryListDto fromEntity(CategoryEntity category) {
        return ProductCategoryListDto.builder()
                .id(category.getId())
                .parentId(category.getParentCategory().getId())
                .parentName(category.getParentCategory().getName())
                .name(category.getName())
                .build();
    }
}
