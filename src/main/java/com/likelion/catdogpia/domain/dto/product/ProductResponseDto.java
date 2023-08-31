package com.likelion.catdogpia.domain.dto.product;

import com.likelion.catdogpia.domain.entity.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class ProductResponseDto {
    private Long id;

    // 카테고리 id
    private Long categoryId;

    // 상품명
    private String name;

    // 가격
    private int price;

    // 사이즈, 색상, 재고
    private List<ProductOptionDto> productOptionList;

    // 썸네일 이미지 URL
    private String thumbnailImage;

    // 내용 이미지 URL
    private String contentImage;

    // TODO 후기, QnA관련
    // 별점 평균
    private int totalRating;

    // 후기 수
    private int reviewCounts;

    // QnA 수
    private int qnACounts;


    public static ProductResponseDto fromEntity(Product product){
        return ProductResponseDto.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .price(product.getPrice())
                .productOptionList(product.getProductOptionList().stream()
                        .map(ProductOptionDto::fromEntity)
                        .collect(Collectors.toList()))
                .thumbnailImage(product.getAttach().getAttachDetailList().get(0).getFileUrl())
                .contentImage(product.getAttach().getAttachDetailList().get(1).getFileUrl())
//                .totalRating(product.calculateTotalRating())  TODO 별점 평균 계산 로직 필요
//                .reviewCounts(product.getReviews().size())  TODO : product에 review 추가 후기 개수 가져오는 로직 필요
//                .qnACounts(product.getQnAList().size())  TODO QnA 개수 가져오는 로직 필요
                .build();
    }
}