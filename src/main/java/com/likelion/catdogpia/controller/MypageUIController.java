package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.mypage.*;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageUIController {

    private final AddressService addressService;
    private final OrderHistoryService orderHistoryService;
    private final PointService pointService;
    private final ReviewService reviewService;
    private final PetService petService;
    private final ProfileService profileService;
    private final MemberArticleService memberArticleService;

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 프로필 페이지
    @GetMapping("/profile")
    public String profilePage() {
        return "page/mypage/profile.html";
    }

    // 회원 정보 수정 페이지
    @GetMapping("/profile/update")
    public String updateProfilePage(Model model) {

        MemberProfileDto memberProfileDto = profileService.getMemberProfile("testtest");
        model.addAttribute("profile", memberProfileDto);
        return "page/mypage/profile_modify.html";
    }

    // 반려동물 등록 페이지
    @GetMapping("/pet")
    public String addPetPage(Model model) {
        return "page/mypage/add_pet.html";
    }

    // 주문 내역 페이지
    @GetMapping("/order-list")
    public String orderListPage(Model model, @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Page<OrderListDto> orderList = orderHistoryService.readAllOrder("testtest", orderStatus, page);
        model.addAttribute("orderList", orderList);
        // 주문 상태별 개수
        model.addAttribute("orderStatusCount", orderHistoryService.getOrderCountByStatus("testtest"));
        return "page/mypage/order_list.html";
    }

    // 주문 내역 > 리뷰 작성 페이지
    @GetMapping("/order-list/review/{opId}")
    public String reviewPage(@PathVariable Long opId, Model model) {
        model.addAttribute("orderProduct", reviewService.getOrderProduct("testtest", opId));
        return "page/mypage/review_write.html";
    }

    // 교환 요청 페이지
    @GetMapping("/order-list/exchange/{opId}")
    public String exchangePage(@PathVariable Long opId, Model model) {
        model.addAttribute("order", orderHistoryService.getOrderInfo("testtest", opId));
        model.addAttribute("option", orderHistoryService.getProductOption(opId));
        return "page/mypage/exchange.html";
    }

    // 환불 요청 페이지
    @GetMapping("/order-list/refund/{opId}")
    public String refundPage(@PathVariable Long opId, Model model) {
        model.addAttribute("order", orderHistoryService.getOrderInfo("testtest", opId));
        return "page/mypage/refund.html";
    }

    // 주문 상세 페이지
    @GetMapping("/order-detail/{orderId}")
    public String orderDetailPage(Model model, @PathVariable Long orderId, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("productList", orderHistoryService.readOrder("testtest", orderId, page, limit));
        model.addAttribute("productDetail", orderHistoryService.readDetail("testtest", orderId));
        return "page/mypage/order_detail.html";
    }

    // 적립금 페이지
    @GetMapping("/point")
    public String pointPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        // 적립금 내역
        model.addAttribute("pointList", pointService.findAllPoint("testtest", page));
        return "page/mypage/point.html";
    }

    // 배송지 관리 페이지
    @GetMapping("/address")
    public String addressPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("addressList", addressService.readAllAddress("testtest", page, limit));
        return "page/mypage/address_list.html";
    }

    // 배송지 등록 페이지
    @GetMapping("/add-address")
    public String addAddressPage(Model model) {
        return "page/mypage/add_address.html";
    }

    // 배송지 수정 팝업 페이지
    @GetMapping("/address/update/{addressId}")
    public String updateAddressPage(@PathVariable Long addressId, Model model) {
        model.addAttribute("address", addressService.readAddress(addressId));
        return "page/mypage/address_modify.html";
    }

    // 리뷰 관리 페이지
    @GetMapping("/review")
    public String reviewPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("reviewList", reviewService.findAllReview("testtest", page));
        return "page/mypage/review_list.html";
    }

    // 리뷰 수정 페이지
    @GetMapping("/review/{reviewId}")
    public String reviewModifyPage(@PathVariable Long reviewId, Model model) {
        model.addAttribute("review", reviewService.getReview("testtest", reviewId));
        return "page/mypage/review_modify.html";
    }

    // 게시글 관리 페이지
    @GetMapping("/article")
    public String mypagePage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        // 게시글 리스트
        Page<MemberArticleListDto> articleListDtoList = memberArticleService.getArticleList("testtest", page);
        model.addAttribute("articleList", articleListDtoList);
        return "page/mypage/article_list.html";
    }

}
