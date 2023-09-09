package com.likelion.catdogpia.domain.dto.mainpage;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewProductListDto {

    // 상품 id (Product)
    private Long id;

    // 상품명 (Product)
    private String name;

    // 가격 (Product)
    private int price;

    // 평점 (Review)

    // 리뷰수 (Review)

    // 상품 이미지 URL (AttachDetail)
    private String fileUrl;

    @Builder
    public NewProductListDto(Long id, String name, int price, String fileUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.fileUrl = fileUrl;
    }
}
