package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.admin.ConsultationDto;
import com.likelion.catdogpia.domain.dto.admin.ConsultationListDto;
import com.likelion.catdogpia.domain.dto.notice.ConsulRequestDto;
import com.likelion.catdogpia.domain.entity.consultation.ConsulClassification;
import com.likelion.catdogpia.domain.entity.product.QnAClassification;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.AdminService;
import com.likelion.catdogpia.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Controller
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;
    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    // 공지사항 목록
    @GetMapping
    public String noticeList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword
    ) {
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("noticeList", noticeService.findNoticeList(pageable, filter, keyword));
        return "/page/notice/notices";
    }

    // 공지사항 상세 조회
    @GetMapping("/{noticeId}")
    public String noticeDetails(@PathVariable Long noticeId, Model model) {
        model.addAttribute("notice", noticeService.findNotice(noticeId));
        return "/page/notice/notice-detail";
    }

    // faq 목록
    @GetMapping("faq")
    public String faqList() {
        return "/page/notice/faq";
    }


    @GetMapping("/consultations-form")
    public String consultationForm() {
        return "/page/notice/consultations";
    }

    // 1:1 문의 목록
    @GetMapping("/consultations")
    @ResponseBody
    public Page<ConsultationListDto> consultationList(
            @RequestHeader("Authorization") String accessToken,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword
    ) {
        if (accessToken == null) {
            throw new RuntimeException();
        }

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        return noticeService.findConsultationList(loginId ,pageable, filter, keyword);
    }

    // 1:1문의 상세
    @GetMapping("/consultations/{consulId}")
    public String consultationDetails(@PathVariable Long consulId, Model model) {
        model.addAttribute("consultation", adminService.findConsultation(consulId));
        return "/page/notice/consultation-detail";
    }

    // 1:1 문의 등록 페이지
    @GetMapping("/consultations/create-form")
    public String consultationCreateForm(Model model) {
        model.addAttribute("classificationList", Arrays.asList(ConsulClassification.values()));
        return "/page/notice/consultation-create";
    }

    // 1:1 문의 등록 페이지
    @PostMapping("/consultations")
    @ResponseBody
    public ResponseEntity<String> consultationCreate(@RequestHeader("Authorization") String accessToken, @RequestBody @Valid ConsulRequestDto requestDto) {

        String result = "ok";
        //
        if (accessToken == null) {
            result = "tokenError";
        }

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        if(requestDto == null) {
            result = "noData";
        }
        noticeService.createConsultation(loginId, requestDto);

        return ResponseEntity.ok(result);
    }
}
