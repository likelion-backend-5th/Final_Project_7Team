package com.likelion.catdogpia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.catdogpia.domain.dto.admin.*;
import com.likelion.catdogpia.domain.dto.notice.NoticeDto;
import com.likelion.catdogpia.domain.entity.consultation.ConsulClassification;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.domain.entity.product.QnAClassification;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.AdminService;
import com.likelion.catdogpia.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;

    // 관리자 메인 페이지
    @GetMapping("/main")
    public String mainPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        model.addAttribute("counts", adminService.findTotalCounts());
        model.addAttribute("memberList", adminService.findMemberList(pageable, null, null));
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
        log.info("member : " + member.toString());
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
    @ResponseBody
    public ResponseEntity<String> memberModify(@RequestHeader("Authorization") String accessToken, @RequestBody MemberDto memberDto, @PathVariable Long memberId) {
        log.info("dto.toString() : " + memberDto.toString());
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        //
        adminService.modifyMember(memberDto, memberId, loginId);
        return ResponseEntity.ok("ok");
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
    @ResponseBody
    public ResponseEntity<String> memberRemove(@RequestHeader("Authorization") String accessToken, @PathVariable Long memberId) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        adminService.removeMember(memberId, loginId);
        return ResponseEntity.ok("ok");
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

    // 상품 등록 페이지
    @GetMapping("/products/create-form")
    public String productCreateForm(Model model) {
        List<CategoryDto> categoryList = adminService.findCategory();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productDto", new ProductDto());
        return "/page/admin/product-create";
    }

    // 상품 등록
    @PostMapping(value = "/products/create")
    @ResponseBody
    public ResponseEntity<String> productCreate(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam("mainImg") MultipartFile mainImg,
            @RequestParam("detailImg") MultipartFile detailImg,
            @RequestParam("productDto") String productDto
    ) throws IOException {

        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        log.info("product : " + productDto);
        log.info("mainImg :" + mainImg.getOriginalFilename());
        log.info("detailImg :" + detailImg.getOriginalFilename());
        // json -> productDto
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto product = objectMapper.readValue(productDto, ProductDto.class);
        log.info("product toString : " + product.toString());

        adminService.createProduct(product, mainImg, detailImg, loginId);

        return ResponseEntity.ok("ok");
    }

    // 상품 수정 페이지
    @GetMapping("/products/{productId}/modify-form")
    public String productModifyForm(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("productDto", adminService.findProduct(productId));
        model.addAttribute("categoryList", adminService.findCategory());
        log.info("product : " + adminService.findProduct(productId).toString());
        return "/page/admin/product-modify";
    }

    // 상품 수정
    @PostMapping("/products/{productId}/modify")
    public ResponseEntity<String> productModify(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long productId,
            @RequestParam(value = "mainImg", required = false) MultipartFile mainImg,
            @RequestParam(value = "detailImg", required = false) MultipartFile detailImg,
            @RequestParam("productDto") String productDto
    ) throws IOException {

        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        log.info("productDto : " + productDto);
        // json -> productDto
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto product = objectMapper.readValue(productDto, ProductDto.class);
        log.info("product toString : " + product.toString());

        adminService.modifyProduct(productId, product, mainImg, detailImg, loginId);

        return ResponseEntity.ok("ok");
    }

    // 상품 삭제
    @PostMapping("/products/{productId}/delete")
    @ResponseBody
    public ResponseEntity<String> productRemove(@RequestHeader("Authorization") String accessToken, @PathVariable Long productId){
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        adminService.removeProduct(productId, loginId);
        return ResponseEntity.ok("ok");
    }

    // 상품명 중복 확인
    @GetMapping("/products/duplicate-check")
    @ResponseBody
    public Map<String, Boolean> checkProductNameAvailability(
            @RequestParam("name") String name,
            @RequestParam(required = false) Long productId
    ){
        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.put("duplicated", adminService.isDuplicatedProductName(name, productId));
        log.info("resultMap : " + resultMap.get("duplicated"));
        return resultMap;
    }

    // 주문관리 목록
    @GetMapping("/orders")
    public String orderList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String orderStatus
    ) {
        log.info("filter : " + filter);
        log.info("keyword : " + keyword);
        log.info("toDate : " + toDate);
        log.info("fromDate : " + fromDate);
        log.info("orderStatus : " + orderStatus);
        model.addAttribute("orderList", adminService.findOrderList(pageable, filter, keyword, toDate, fromDate, orderStatus));
        model.addAttribute("filter",filter);
        model.addAttribute("keyword",keyword);
        model.addAttribute("toDate",toDate);
        model.addAttribute("fromDate",fromDate);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("orderStatusList", Arrays.asList(OrderStatus.values()));
        return "/page/admin/orders";
    }

    // 주문 상태 변경
    @PostMapping("/orders/change-status")
    @ResponseBody
    public ResponseEntity<String> changeOrderStatus(@RequestHeader("Authorization") String accessToken, @RequestBody List<OrderStatusUpdateDto> updateDtoList) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        // 백단에서 한번 더 list validation check 수행
        if(updateDtoList.isEmpty()) {
            throw new IllegalArgumentException("list is empty");
        }
        else {
            adminService.changeOrderStatus(updateDtoList, loginId);
        }

        return ResponseEntity.ok("ok");
    }

    // 주문내역상세 조회 및 수정
    @GetMapping("/orders/{orderId}/modify-form")
    public String orderModifyForm(@PathVariable Long orderId, Model model) {
        List<OrderDto> order = adminService.findOrder(orderId);

        for (OrderDto orderProductDto : order) {
            log.info(orderProductDto.toString());
        }
        // 해당 주문에 대한 정보가 없으면 오류 발생
        if(order.isEmpty()) {
            throw new IllegalArgumentException("list is empty");
        }

        model.addAttribute("orderStatusList", Arrays.asList(OrderStatus.values()));
        model.addAttribute("order", order);

        return "/page/admin/order-modify";
    }

    // 커뮤니티 목록
    @GetMapping("/communities")
    public String communityList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword
    ) {
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("communityList", adminService.findCommunityList(pageable, filter, keyword));

        return "/page/admin/communities";
    }

    // 커뮤니티 삭제
    @PostMapping("/communities/delete-list")
    @ResponseBody
    public ResponseEntity<String> communitiesDelete(@RequestHeader("Authorization") String accessToken, @RequestBody List<Map<String, Object>> requestList) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        // 한번더 체크
        if(requestList.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            List<Long> deleteList = new ArrayList<>();
            // deleteList 생성
            for (Map<String, Object> map : requestList) {
                deleteList.add(Long.valueOf((String) map.get("id")));
            }
            adminService.deleteCommunities(deleteList, loginId);
        }

        return ResponseEntity.ok("ok");
    }

    // 커뮤니티 상세 조회
    @GetMapping("/communities/{communityId}")
    public String communityDetails(
            @PathVariable Long communityId,
            Model model) {
        model.addAttribute("community", adminService.findCommunity(communityId));
        return "/page/admin/community-detail";
    }

    // 커뮤니티 댓글 조회
    @GetMapping("/communities/{communityId}/comments")
    @ResponseBody
    public Page<CommentDto> commentList(@PathVariable Long communityId, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<CommentDto> list = adminService.findCommentList(communityId, pageable);
        log.info("hi : " + list);
        return list;
    }

    // 댓글 삭제
    @PostMapping("/communities/{communityId}/comments/{commentId}")
    @ResponseBody
    public ResponseEntity<String> commentDelete(@RequestHeader("Authorization") String accessToken,  @PathVariable Long communityId, @PathVariable Long commentId) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        adminService.deleteComment(communityId, commentId, loginId);
        return  ResponseEntity.ok("ok");
    }

    // 댓글 등록
    @PostMapping("/communities/{communityId}/comments/create")
    public ResponseEntity<String> commentCreate(@RequestHeader("Authorization") String accessToken,  @PathVariable Long communityId, @RequestBody String content) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        adminService.createComment(communityId, content, loginId);
        return ResponseEntity.ok("ok");
    }

    // QnA목록
    @GetMapping("/qna")
    public String qnaList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String fromDate
    ) {
        Page<QnaListDto> qnaList = adminService.findQnaList(pageable, filter, keyword, toDate, fromDate);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("toDate", toDate);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("classificationList", Arrays.asList(QnAClassification.values()));
        return "/page/admin/qna";
    }

    // QnA 삭제
    @PostMapping("/qna/delete-list")
    @ResponseBody
    public ResponseEntity<String> qnaDelete(@RequestHeader("Authorization") String accessToken, @RequestBody List<Map<String, Object>> requestList) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        // 한번더 체크
        if(requestList.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            List<Long> deleteList = new ArrayList<>();
            // deleteList 생성
            for (Map<String, Object> map : requestList) {
                deleteList.add(Long.valueOf((String) map.get("id")));
            }
            adminService.deleteQnaList(deleteList,loginId);
        }

        return ResponseEntity.ok("ok");
    }

    // QnA 상세
    @GetMapping("/qna/{qnaId}")
    public String qnaDetails(@PathVariable Long qnaId, Model model) {
        model.addAttribute("classificationList", Arrays.asList(QnAClassification.values()));
        model.addAttribute("qna", adminService.findQna(qnaId));
        return "/page/admin/qna-detail";
    }

    // QnA 답변 등록 / 업데이트
    @PostMapping("/qna/{qnaId}/update-answer")
    @ResponseBody
    public ResponseEntity<String> qnaUpdateAnswer(@RequestHeader("Authorization") String accessToken, @PathVariable Long qnaId, @RequestBody Map<String, String> requestBody) {
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        String answer = requestBody.get("answer");
        adminService.modifyQnaAnswer(qnaId, answer, loginId);

        return ResponseEntity.ok("ok");
    }

    // 1:1문의 목록
    @GetMapping("/consultations")
    public String consultationsList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String fromDate
    ) {
        model.addAttribute("consultationList", adminService.findConsultationList(pageable, filter, keyword, toDate, fromDate));
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("toDate", toDate);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("classificationList", Arrays.asList(ConsulClassification.values()));
        return "/page/admin/consultations";
    }

    // 1:1 문의 삭제
    @PostMapping("/consultations/delete-list")
    @ResponseBody
    public ResponseEntity<String> consultationsDelete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody List<Map<String, Object>> requestList
    ) {
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        // 한번더 체크
        if(requestList.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            List<Long> deleteList = new ArrayList<>();
            // deleteList 생성
            for (Map<String, Object> map : requestList) {
                deleteList.add(Long.valueOf((String) map.get("id")));
            }
            adminService.deleteConsultationList(deleteList, loginId);
        }

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    // 1:1 문의 상세
    @GetMapping("/consultations/{consulId}")
    public String consultationDetails(@PathVariable Long consulId, Model model) {
        model.addAttribute("classificationList", Arrays.asList(ConsulClassification.values()));
        model.addAttribute("consultation", adminService.findConsultation(consulId));
        return "/page/admin/consultation-detail";
    }

    // 1:1 문의 답변 등록 / 업데이트
    @PostMapping("/consultations/{consulId}/update-answer")
    @ResponseBody
    public ResponseEntity<String> consultationUpdateAnswer(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long consulId,
            @RequestBody Map<String, String> requestBody
    ){
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        //
        String answer = requestBody.get("answer");
        adminService.modifyConsultationAnswer(consulId, answer, loginId);
        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    // 신고 관리 목록
    @GetMapping("/reports")
    public String reportList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String fromDate
    ){
        model.addAttribute("reportList", adminService.findReportList(pageable, filter, keyword, toDate, fromDate));
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("toDate", toDate);
        model.addAttribute("fromDate", fromDate);
        return "/page/admin/reports";
    }

    // 신고 삭제
    @PostMapping("/reports/delete-list")
    @ResponseBody
    public ResponseEntity<String> reportDelete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody List<Map<String, Object>> requestList
    ) {
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        // 한번더 체크
        if(requestList.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            List<Long> deleteList = new ArrayList<>();
            // deleteList 생성
            for (Map<String, Object> map : requestList) {
                deleteList.add(Long.valueOf((String) map.get("id")));
            }
            adminService.deleteReportList(deleteList, loginId);
        }

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    // 신고 상세
    @GetMapping("/reports/{reportId}")
    public String reportDetails(@PathVariable Long reportId, Model model) {
        model.addAttribute("report", adminService.findReport(reportId));
        return "/page/admin/report-detail";
    }

    // 신고 처리
    @PostMapping("/reports/{reportId}/processed")
    @ResponseBody
    public ResponseEntity<String> reportProcessed(@RequestHeader("Authorization") String accessToken, @PathVariable Long reportId){
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        //
        adminService.processedReport(loginId, reportId);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }


    // 공지사항 목록
    @GetMapping("/notices")
    public String noticeList(
            Model model,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword
    ) {
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("noticeList", noticeService.findNoticeList(pageable, filter, keyword));
        return "/page/admin/notices";
    }

    // 공지사항 등록 페이지
    @GetMapping("/notices/create-form")
    public String noticeCreateForm() {
        return "/page/admin/notice-create";
    }

    // 공지사항 등록
    @PostMapping("/notices/create")
    public ResponseEntity<String> noticeCreate(@RequestHeader("Authorization") String accessToken, @RequestBody NoticeDto noticeDto) {
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        //
        noticeService.noticeCreate(loginId, noticeDto);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    // 공지사항 상세
    @GetMapping("/notices/{noticeId}")
    public String noticeDetails(@PathVariable Long noticeId, Model model) {
        model.addAttribute("notice", noticeService.findNotice(noticeId));
        return "/page/admin/notice-detail";
    }

    // 공지사항 수정 페이지
    @GetMapping("/notices/{noticeId}/modify-form")
    public String noticeModifyForm(@PathVariable Long noticeId, Model model) {
        model.addAttribute("notice", noticeService.findNotice(noticeId));
        return "/page/admin/notice-modify";
    }

    // 공지사항 수정
    @PostMapping("/notices/{noticeId}/modify")
    public ResponseEntity<String> noticesModify(@PathVariable Long noticeId, @RequestHeader("Authorization") String accessToken, @RequestBody NoticeDto noticeDto) {
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 토큰에서 loginId 가져옴
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        //
        noticeService.noticeModify(loginId, noticeId, noticeDto);

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

    // 공지사항 삭제
    @PostMapping("/notices/delete-list")
    public ResponseEntity<String> noticeDelete(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody List<Map<String, Object>> requestList
    ) {
        // 토큰이 없으면 오류 발생
        if(accessToken == null){
            throw new RuntimeException();
        }
        // 한번더 체크
        if(requestList.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            List<Long> deleteList = new ArrayList<>();
            // deleteList 생성
            for (Map<String, Object> map : requestList) {
                deleteList.add(Long.valueOf((String) map.get("id")));
            }
            noticeService.deleteNoticeList(deleteList);
        }

        return ResponseEntity.ok(HttpStatus.OK.name());
    }

}