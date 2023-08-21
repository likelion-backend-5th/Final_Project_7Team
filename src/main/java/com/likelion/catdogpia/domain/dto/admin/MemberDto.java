package com.likelion.catdogpia.domain.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String name;
    private String loginId;
    private String nickname;
    private char blackListYn;
    private LocalDateTime createdAt;
}
