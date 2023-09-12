package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberProfileDto {

    // 아이디
    private String loginId;

    // 이름
    private String name;

    // 휴대폰 번호
    private String phone;
    
    // 이메일
    private String email;
    
    // 닉네임
    private String nickname;

    @Builder
    public MemberProfileDto(String loginId, String name, String phone, String email, String nickname) {
        this.loginId = loginId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
    }
}
