package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.admin.MemberDto;
import com.likelion.catdogpia.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    // 관리자 메인 페이지
    @GetMapping("/main")
    public String mainPage() {
        return "/page/admin/index";
    }

    // 관리자 회원관리 목록
    @GetMapping("/members")
    public String memberList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword
    ) {
        model.addAttribute("memberList", adminService.findMemberList(pageable, filter, keyword));
        model.addAttribute("filter",filter);
        model.addAttribute("keyword",keyword);
        return "/page/admin/members";
    }

    // 관리자 회원 관리 상세
    @GetMapping("/members/{memberId}")
    public String memberDetails(@PathVariable Long memberId, Model model) {

        MemberDto member = adminService.findMember(memberId);
        // 금액 포맷 맞추기
        member.changeFormat(member);
        model.addAttribute("member", member);
        return "/page/admin/member-detail";
    }

    // 관리자 회원 수정 페이지
    @GetMapping("/members/{memberId}/modify-form")
    public String memberModifyForm(@PathVariable Long memberId, Model model) {
        MemberDto member = adminService.findMember(memberId);

        member.changeFormat(member);
        model.addAttribute("member", member);
        return "/page/admin/member-update";
    }

    // 사용자 정보 수정
    @PostMapping("/members/{memberId}/modify")
    public String memberModify(@ModelAttribute("member") MemberDto memberDto, @PathVariable Long memberId) {
        log.info("dto.toString() : " + memberDto.toString());
        adminService.modifyMember(memberDto, memberId);
        return "redirect:/admin/members";
    }

    // 이메일, 닉네임 중복 확인
    @GetMapping("/members/check-duplicate")
    @ResponseBody // JSON 응답을 위한 어노테이션 추가
    public Map<String, Boolean> checkEmailAvailability(
            @RequestParam("email") String email,
            @RequestParam("nickname") String nickname,
            @RequestParam("memberId") Long memberId
    ) {
        log.info("email : " + email);
        log.info("nickname : " + nickname);
        log.info("memberId : " + memberId);
        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put("duplicated", adminService.isDuplicated(email, nickname, memberId));
        log.info("duplicated : " + resultMap.get("duplicated"));
        return resultMap;
    }

    // 회원 삭제
    @PostMapping("/members/{memberId}/delete")
    public String memberRemove(@PathVariable Long memberId) {
        log.info("delete");
        adminService.removeMember(memberId);
        return "redirect:/admin/members";
    }

    // 상품 목록
    @GetMapping("/products")
    public String productList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword
    ) {
        model.addAttribute("productList", adminService.findProductList(pageable, filter, keyword));
        model.addAttribute("filter",filter);
        model.addAttribute("keyword",keyword);
        return "/page/admin/products";
    }

    // 상품 상세
    @GetMapping("/products/{productId}")
    public String productDetails(@PathVariable Long productId, Model model) {

        return "/page/admin/product-detail";
    }
}