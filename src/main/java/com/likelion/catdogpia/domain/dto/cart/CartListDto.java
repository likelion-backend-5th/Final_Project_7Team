package com.likelion.catdogpia.domain.dto.cart;

import com.likelion.catdogpia.domain.entity.attach.Attach;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CartListDto {

    // Cart id (Cart)
    private Long id;

    // 상품명 (Product)
    private String name;

    // 옵션 - size (ProductOption)
    private String size;

    // 옵션 - color (ProductOption)
    private String color;

    // 가격 (Product)
    private int price;

    // 수량 (Cart)
    private int productCnt;

    // 상품 총 금액
    private int amount;

    // 재고 (ProductOption)
    private int stock;

    // 상품 이미지
    private String fileUrl;

    @Builder
    public CartListDto(Long id, String name, String size, String color, int price, int productCnt, int amount, int stock, String fileUrl) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.color = color;
        this.price = price;
        this.productCnt = productCnt;
        this.amount = amount;
        this.stock = stock;
        this.fileUrl = fileUrl;
    }
}
