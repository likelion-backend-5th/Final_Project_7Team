package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.jwt.domain.JwtTokenResponseDto;
import com.likelion.catdogpia.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    //로그인 UI
    @GetMapping("/login")
    public String loginForm() {
        return "/page/login/login-form";
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginJwt(@RequestParam("loginId")String loginId, @RequestParam("password")String password, HttpServletResponse httpResponse) {
        String result = loginService.login(loginId, password);
        if (result.equals("fail")) {
            Map<String, String> response = new HashMap<>();
            response.put("result", "fail");
            return ResponseEntity.ok(response);
        }
        JwtTokenResponseDto jwt = jwtTokenProvider.createTokensByLogin(loginId);
        String accessToken = jwt.getAccessToken();
        String refreshToken = jwt.getRefreshToken();

        //AccessToken LocalStorage
        Map<String, String> response = new HashMap<>();
        response.put("result", "success");
        response.put("accessToken", accessToken);

        //RefreshToken Cookie
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(24*60*60);
        refreshTokenCookie.setPath("/");
        httpResponse.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(response);
    }

    //아이디찾기 UI
    @GetMapping("/find/id")
    public String findIdForm() {
        return "/page/login/findId-form";
    }

    //아이디찾기
    @GetMapping("/findId")
    @ResponseBody
    public Map<String, String> findId(@RequestParam("name")String name, @RequestParam("email")String email) {
        String message = loginService.findId(name, email);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    //비밀번호찾기 UI
    @GetMapping("/find/password")
    public String findPwForm() {
        return "/page/login/findPw-form";
    }

    //비밀번호찾기 (계정 확인)
    @GetMapping("/findPassword")
    @ResponseBody
    public Map<String, String> findPassword(@RequestParam("loginId")String loginId, @RequestParam("name")String name, @RequestParam("email")String email) {
        String message = loginService.findPassword(loginId, name, email);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    //비밀번호찾기 (임시 비밀번호 발급)
    @PostMapping("/tempPassword")
    @ResponseBody
    public String sendTempPassword(String email) {
        return loginService.sendTempPassword(email);
    }
}