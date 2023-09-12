package com.likelion.catdogpia.domain.dto.mainpage;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HotProductListDto {

    // 상품 id (Product)
    private Long id;

    // 상품명 (Product)
    private String name;

    // 상품 이미지 URL (AttachDetail)
    private String fileUrl;

    @Builder
    public HotProductListDto(Long id, String name, String fileUrl) {
        this.id = id;
        this.name = name;
        this.fileUrl = fileUrl;
    }
}
