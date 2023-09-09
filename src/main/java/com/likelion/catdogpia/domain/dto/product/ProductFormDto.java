package com.likelion.catdogpia.domain.dto.product;

import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.product.ProductOption;
import com.likelion.catdogpia.domain.entity.product.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class ProductFormDto {
    //카테고리 대표이미지 추가이미지

    // 상품명
    private String name;

    // 가격
    private int price;

    // 판매 상태
    private String status;

    // size, stock, color
    private List<ProductOptionDto> productOptions;

    private String productThumbnailUrl;

    private String thumbnailImage;

    private String contentImage;

    private Long category;  // category PK ex. pk:4(name:셔츠) select * from category where category_id = 4;

    public Product toEntity(){
        List<ProductOption> options = productOptions.stream()
                .map(optionDto -> ProductOption.builder()
                        .size(optionDto.getSize())
                        .color(optionDto.getColor())
                        .stock(optionDto.getStock())
                        .build())
                .collect(Collectors.toList());

        return Product.builder()
                .name(name)
                .price(price)
                .status(ProductStatus.valueOf(status))
                .productOptionList(options)
                // TODO Images, categories
//                .category(productCategories)
                .build();
    }

}
