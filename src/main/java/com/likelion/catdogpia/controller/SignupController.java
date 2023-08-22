package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.entity.user.CustomMemberDetails;
import com.likelion.catdogpia.domain.entity.user.Role;
import com.likelion.catdogpia.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class SignupController {
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    private final MemberService service;

    public SignupController(UserDetailsManager manager, PasswordEncoder passwordEncoder, MemberService service) {
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    //회원가입 UI
    @GetMapping("/signup")
    public String signupForm() {
        return "/page/signup/signup-form";
    }

    //회원가입
    @PostMapping("/signup")
    public String signupPost(
            @RequestParam("loginId")String loginId,
            @RequestParam("password")String password,
            @RequestParam("checkPassword")String checkPassword,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("email")String email,
            @RequestParam("nickname")String nickname)
    {
        if (password.equals(checkPassword)) {
            log.info("비밀번호 일치");
            manager.createUser(CustomMemberDetails.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .phone(phone)
                    .email(email)
                    .nickname(nickname)
                    .role(Role.USER)
                    .socialLogin('N')
                    .blackListYn('N')
                    .build());
            return "redirect:/page/login/login-form";
        }
        log.warn("비밀번호 불일치");
        return "redirect:/signup";
    }

    //아이디 중복확인
    @GetMapping("/checkDuplicateId")
    @ResponseBody
    public Map<String, Boolean> checkDuplicateId(@RequestParam("loginId") String loginId) {
        boolean isDuplicate = manager.userExists(loginId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return response;
    }

    //닉네임 중복확인
    @GetMapping("/checkDuplicateNickname")
    @ResponseBody
    public Map<String, Boolean> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicate = service.nicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return response;
    }
}
