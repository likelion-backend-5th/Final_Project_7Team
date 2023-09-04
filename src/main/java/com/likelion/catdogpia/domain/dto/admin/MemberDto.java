package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.user.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String name;
    private String loginId;
    private String nickname;
    private String phone;
    private String email;
    private String address;
    private Role role;
    private LocalDateTime createdAt;
    private char blackListYn;
    private Long totalPoint;
    private Long totalReportCnt;
    private Long totalAmount;
    private String formatPoint;
    private String formatAmount;

    @Builder
    public MemberDto(Long id, String name, String loginId, String nickname, String phone, String email, String address, Role role, LocalDateTime createdAt, char blackListYn, Long totalPoint, Long totalReportCnt, Long totalAmount) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.createdAt = createdAt;
        this.blackListYn = blackListYn;
        this.totalPoint = totalPoint;
        this.totalReportCnt = totalReportCnt;
        this.totalAmount = totalAmount;
    }

    // 금액 포맷 맞추기
    public void changeFormat(MemberDto memberDto){
        this.formatPoint = String.format("%,d원", memberDto.totalPoint);
        this.formatAmount = String.format("%,d원", memberDto.totalAmount);
    }
}
