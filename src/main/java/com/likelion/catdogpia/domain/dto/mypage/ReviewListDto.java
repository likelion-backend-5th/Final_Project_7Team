package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewListDto {

    // 리뷰 id (Review)
    private Long id;

    // 내용 (Review)
    private String description;

    // 이미지 파일 url (1번째 파일) (AttachDetail)
    private String fileUrl;

    // 기타 이미지 개수
    private Long otherImgCnt;

    // 상품명 (Product)
    private String pname;

    // 옵션 - size (ProductOption)
    private String size;

    // 옵션 - color (ProductOption)
    private String color;

    // 작성일자 (Review)
    private LocalDateTime createdAt;

    // 평점 (Review)
    private int rating;

    @Builder
    public ReviewListDto(Long id, String description, String fileUrl, Long otherImgCnt, String pname, String size, String color, LocalDateTime createdAt, int rating) {
        this.id = id;
        this.description = description;
        this.fileUrl = fileUrl;
        this.otherImgCnt = otherImgCnt;
        this.pname = pname;
        this.size = size;
        this.color = color;
        this.createdAt = createdAt;
        this.rating = rating;
    }
}
