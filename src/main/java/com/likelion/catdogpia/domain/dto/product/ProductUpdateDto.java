package com.likelion.catdogpia.domain.dto.product;


import com.likelion.catdogpia.domain.entity.CategoryEntity;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductUpdateDto {
    //카테고리 대표이미지 추가이미지

    // 상품명
    private String name;

    // 가격
    private int price;

    // 내용
    private String content;

    // 판매 상태
    private String status;

    // 카테고리
    private String category;

    // TODO - List or 각각 객체로 가져와서 각자 toEntity 작성

    // size, stock, color
    private List<ProductOptionDto> productOptions;

    // 카테고리가 여러 개인 이유..?
    private List<CategoryEntity> productCategories;

    private List<AttachDetail> productImages;
}
