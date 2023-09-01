package com.likelion.catdogpia.domain.dto.product;

import com.likelion.catdogpia.domain.entity.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductListResponseDto {
    private Long id;

    // 상품명
    private String name;

    // 가격
    private int price;

    // 별점 평균
    private int rating;

    // 후기 수
    private int reviewCounts;

    // 썸네일 URL
    private String thumbnailImage;

    public static ProductListResponseDto fromEntity(Product product) {
        return ProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                // TODO
//                .rating()
//                .reviewCounts()
//                .thumbnailImage()
                .build();
    }
}
