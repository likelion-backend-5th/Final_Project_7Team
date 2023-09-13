package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewUpdateDto {

    // 리뷰 id
    private Long reviewId;

    // 내용
    private String description;

    // 평점
    private int rating;

    // 이미지 파일
    private List<AttachDetail> attachDetailList;

    public static ReviewUpdateDto fromEntity(Review entity) {
        ReviewUpdateDto.ReviewUpdateDtoBuilder builder = ReviewUpdateDto.builder()
                .reviewId(entity.getId())
                .description(entity.getDescription())
                .rating(entity.getRating());

        if (entity.getAttach() != null) {
            builder.attachDetailList(entity.getAttach().getAttachDetailList());
        }

        return builder.build();
    }
}
