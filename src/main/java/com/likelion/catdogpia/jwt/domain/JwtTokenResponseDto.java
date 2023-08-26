package com.likelion.catdogpia.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenResponseDto {
    private final String accessToken;
    private final String refreshToken;
}
