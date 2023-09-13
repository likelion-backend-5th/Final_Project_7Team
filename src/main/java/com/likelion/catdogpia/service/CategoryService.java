package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.category.ProductCategoryDto;
import com.likelion.catdogpia.domain.dto.category.ProductMainCategoryDto;
import com.likelion.catdogpia.domain.entity.CategoryEntity;
import com.likelion.catdogpia.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ProductCategoryDto> getCategoryList(Long mainCategoryId) {
        List<CategoryEntity> categoryList = categoryRepository.findByParentCategory_Id(mainCategoryId);

        return categoryList.stream()
                .map(ProductCategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductMainCategoryDto getMainCategory(Long mainCategoryId) {
        CategoryEntity category = categoryRepository.findById(mainCategoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ProductMainCategoryDto.fromEntity(category);
    }
}
