package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class LoginController {
    private final MemberService service;

    public LoginController(MemberService service) {
        this.service = service;
    }

    //로그인 UI
    @GetMapping("/login")
    public String loginForm() {
        return "/page/login/login-form";
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
        String message = service.findId(name, email);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    //비밀번호찾기 UI
    @GetMapping("/find/password")
    public String findPwForm() {
        return "/page/login/findPw-form";
    }
}
