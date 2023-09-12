package com.likelion.catdogpia.domain.dto.mypage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberModifyFormDto {

    // 아이디
    private Long id;

    // 이름
    private String name;

    // 기존 비밀번호

    // 새 비밀번호
    private String password;

//    // 새 비밀번호 확인
//    private String  passwordCheck;

    // 휴대폰번호
    private String phone;

    // 이메일
//    private String email;

    // 닉네임
    private String nickname;

}
