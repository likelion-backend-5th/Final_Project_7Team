package com.likelion.catdogpia.domain.dto.product;

import java.util.List;

public class ProductResponseDto {
    // 재고 ??
    private Long id;

    // 상품명
    private String name;

    // 가격
    private int price;

    // 별점 평균
    private int totalRating;

    // 후기 수
    private int reviewCounts;

    // QnA 수
    private int qnACounts;

    // 색상
    private String color;

    // 사이즈
    private String size;

    // 이미지 URL
    private List<String> images;
}
