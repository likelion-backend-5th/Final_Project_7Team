package com.likelion.catdogpia.jwt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Duration refreshTokenValidTime;

    private String loginId;

    @Builder
    public RefreshToken(String token, Duration refreshTokenValidTime, String loginId) {
        this.token = token;
        this.refreshTokenValidTime = refreshTokenValidTime;
        this.loginId = loginId;
    }

    public void setRefreshToken(String token) {
        this.token = token;
    }
}