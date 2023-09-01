package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.product.ProductListResponseDto;
import com.likelion.catdogpia.domain.dto.product.ProductResponseDto;
import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.repository.CategoryRepository;
import com.likelion.catdogpia.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // 대분류일 경우 조회
    public Page<ProductListResponseDto> getProductList(Pageable pageable, Long MainCategoryId, String searchKeyword) {
        if (searchKeyword == null) {
            searchKeyword = "";
        }
        Page<Product> productPage = productRepository.findByCategoryProduct(MainCategoryId, pageable, searchKeyword);
        return productPage.map(ProductListResponseDto::fromEntity);
    }

    // 소분류 조회 및 검색
    public Page<ProductListResponseDto> getProductSearchList(Pageable pageable, Long categoryId, String searchKeyword) {
        if (searchKeyword == null) {
            searchKeyword = "";
        }
        Page<Product> productPage = productRepository.findBySearchProduct(categoryId, pageable, searchKeyword);
        return productPage.map(ProductListResponseDto::fromEntity);
    }

    public ProductResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ProductResponseDto.fromEntity(product);
    }
}
