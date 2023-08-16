package com.likelion.catdogpia.domain.entity.user;

import com.likelion.catdogpia.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_id")
    private Long memberId;

    @Column(name = "login_id", length = 20, nullable = false, unique = true)
    private String loginId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 60, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false, unique = true)
    private String nickname;

    @Column(length = 11, nullable = false, unique = true)
    private String phone;

    @Column(length = 5, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "social_login", length = 1)
    private Character socialLogin;

    @Column(name = "blacklist_yn", length = 1, nullable = false)
    private Character blackListYn;

    @Builder
    public Member(Long memberId, String loginId, String password, String name, String email, String nickname, String phone, Role role, Character socialLogin, Character blackListYn) {
        this.memberId = memberId;
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
}
