package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {

    private String message;

    @Builder
    public ResponseDto(String message) {
        this.message = message;
    }
}
