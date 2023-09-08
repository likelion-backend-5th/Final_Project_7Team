package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.product.ProductOption;
import com.likelion.catdogpia.domain.entity.product.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {

    private Long id;
    private Long categoryId;
    private Long parentCategoryId;
    private String name;
    private ProductStatus status;
    private int price;
    private List<ProductOptionDto> productOptionList;
    private List<AttachDetailDto> attachDetailList;

    //== Entity -> DTO ==//
    public static ProductDto fromEntity(Product product) {

        List<ProductOptionDto> productOptionList = new ArrayList<>();
        List<AttachDetailDto> attachDetailList = new ArrayList<>();

        if(product.getProductOptionList() != null){
            for (ProductOption productOption : product.getProductOptionList()) {
                productOptionList.add(ProductOptionDto.fromEntity(productOption));
            }
        }

        if(product.getAttach() != null) {
            for (AttachDetail attachDetail : product.getAttach().getAttachDetailList()) {
                attachDetailList.add(AttachDetailDto.fromEntity(attachDetail));
            }
        }

        return ProductDto.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .parentCategoryId(product.getCategory().getParentCategory().getId())
                .name(product.getName())
                .status(product.getStatus())
                .price(product.getPrice())
                .productOptionList(productOptionList)
                .attachDetailList(attachDetailList)
                .build();
    }

    @Builder
    public ProductDto(Long id, Long categoryId, Long parentCategoryId, String name, ProductStatus status, int price, List<ProductOptionDto> productOptionList, List<AttachDetailDto> attachDetailList) {
        this.id = id;
        this.categoryId = categoryId;
        this.parentCategoryId = parentCategoryId;
        this.name = name;
        this.status = status;
        this.price = price;
        this.productOptionList = productOptionList;
        this.attachDetailList = attachDetailList;
    }
}
