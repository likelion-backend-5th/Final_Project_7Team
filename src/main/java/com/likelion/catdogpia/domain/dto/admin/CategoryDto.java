package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.CategoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private Long categoryId;
    private String name;

    //== Entity -> DTO ==//
    public static CategoryDto fromEntity(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .categoryId(categoryEntity.getParentCategory() == null ? null : categoryEntity.getParentCategory().getId())
                .name(categoryEntity.getName())
                .build();
    }

    @Builder
    public CategoryDto(Long id, Long categoryId, String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }
}
