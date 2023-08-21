package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.admin.MemberListDto;
import com.likelion.catdogpia.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/main")
    public String mainPage() {
        return "/page/admin/index";
    }

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

    @GetMapping("/members/{memberId}")
    public String memberDetails(@PathVariable Long memberId, Model model) {
        model.addAttribute("member", adminService.findMember(memberId));
        return "/page/admin/member-detail";
    }
}
