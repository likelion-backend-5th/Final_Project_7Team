package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.product.Product;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {

    private Long id;
    private Long categoryId;
    private String name;
    private String status;
    private int price;
    private List<ProductOptionDto> productOptionList;

    @Builder
    public ProductDto(Long id, Long categoryId, String name, String status, int price, List<ProductOptionDto> productOptionList) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
        this.price = price;
        this.productOptionList = productOptionList;
    }
}
