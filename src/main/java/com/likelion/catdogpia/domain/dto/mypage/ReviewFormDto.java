package com.likelion.catdogpia.domain.dto.mypage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewFormDto {

    // 내용
    @NotBlank
    @Size(min=20)
    private String description;

    // 평점
    private int rating;

}
