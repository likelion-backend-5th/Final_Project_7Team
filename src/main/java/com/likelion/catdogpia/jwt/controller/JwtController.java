package com.likelion.catdogpia.jwt.controller;

import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.jwt.domain.JwtTokenResponseDto;
import com.likelion.catdogpia.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    //토큰 검증
    @GetMapping("/authorize")
    public Map<String, String> authorize(@RequestHeader("Authorization") String accessToken) {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        }
        response.put("result", "성공");
        return response;
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public Map<String, String> reissue(@CookieValue("RefreshToken") String refreshToken, HttpServletResponse httpResponse) {
        Map<String, String> response = new HashMap<>();
        if (refreshToken == null) {
            response.put("result", "fail");
        } else {
            JwtTokenResponseDto newJwt = jwtTokenProvider.reissue(refreshToken);

            //AccessToken LocalStorage
            response.put("result", "success");
            response.put("accessToken", newJwt.getAccessToken());

            //RefreshToken Cookie
            Cookie refreshTokenCookie = new Cookie("RefreshToken", newJwt.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(24*60*60);
            refreshTokenCookie.setPath("/");
            httpResponse.addCookie(refreshTokenCookie);
        }
        return response;
    }
}
