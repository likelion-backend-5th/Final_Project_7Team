package com.likelion.catdogpia.domain.entity.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor
public class CustomMemberDetails implements UserDetails {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String nickname;
    private String phone;
    private Role role;
    private Character socialLogin;
    private Character blackListYn;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public CustomMemberDetails(String loginId, String password, String name, String email, String nickname, String phone, Role role, Character socialLogin, Character blackListYn) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.role = role;
        this.socialLogin = socialLogin;
        this.blackListYn = blackListYn;
    }

    public static CustomMemberDetails fromEntity(Member member) {
        return CustomMemberDetails.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .role(member.getRole())
                .socialLogin(member.getSocialLogin())
                .blackListYn(member.getBlackListYn())
                .build();
    }

    public Member newMember() {
        Member member = Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .nickname(nickname)
                .phone(phone)
                .role(Role.USER)
                .socialLogin(socialLogin)
                .blackListYn(blackListYn)
                .build();
        return member;
    }
}
