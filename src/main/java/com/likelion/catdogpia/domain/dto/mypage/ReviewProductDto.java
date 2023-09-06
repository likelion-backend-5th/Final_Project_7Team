package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewProductDto {

    // OrderProduct id (OrderProduct)
    private Long id;

    // 상품명 (Product)
    private String pname;

    // 옵션 - 컬러 (ProductOption)
    private String color;

    // 옵션 - 사이즈 (ProductOption)
    private String size;

    // 이미지 파일 url (AttachDetail)
    private String fileUrl;

    @Builder
    public ReviewProductDto(Long id, String pname, String color, String size) {
        this.id = id;
        this.pname = pname;
        this.color = color;
        this.size = size;
    }
}
