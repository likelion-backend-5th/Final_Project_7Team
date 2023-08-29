package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.user.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberListDto {

    private Long id;
    private String name;
    private String loginId;
    private String nickname;
    private char blackListYn;
    private LocalDateTime createdAt;

    @Builder
    public MemberListDto(Long id, String name, String loginId, String nickname, char blackListYn, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.nickname = nickname;
        this.blackListYn = blackListYn;
        this.createdAt = createdAt;
    }

    //== Entity -> DTO ==//
    public static MemberListDto fromEntity(Member member){
        return MemberListDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .blackListYn(member.getBlackListYn())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
