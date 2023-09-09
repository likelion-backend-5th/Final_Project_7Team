package com.likelion.catdogpia.jwt.controller;

import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.jwt.domain.JwtTokenResponseDto;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtController {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    //토큰 검증
    @GetMapping("/authorize")
    public Map<String, String> authorize(@RequestHeader("Authorization") String accessToken) {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        }
        response.put("result", "성공");
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        response.put("loginId", loginId);

        Optional<Member> member = memberRepository.findByLoginId(loginId);
        response.put("nickname", member.get().getNickname());
        response.put("role", String.valueOf(member.get().getRole()));
        return response;
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public Map<String, String> reissue(@CookieValue("RefreshToken") String refreshToken, HttpServletResponse httpResponse) {
        Map<String, String> response = new HashMap<>();
        if (refreshToken == null) {
            response.put("result", "fail");

            log.info("토큰 재발급 완료");
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

            log.info("토큰 재발급 완료");
        }
        return response;
    }

    //로그아웃
    @GetMapping("/signout")
    public Map<String, String> logout(@RequestHeader("Authorization") String accessToken, HttpServletResponse httpResponse) {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        } else {
            String token = accessToken.split(" ")[1];
            String loginId = jwtTokenProvider.parseClaims(token).getSubject();

            // Refresh Token DB 삭제
            loginService.logout(loginId);

            // Refresh Token 쿠키 삭제
            Cookie refreshTokenCookie = new Cookie("RefreshToken", null);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(0);
            refreshTokenCookie.setPath("/");
            httpResponse.addCookie(refreshTokenCookie);

            response.put("result", "성공");
        }
        return response;
    }
}
