package com.likelion.catdogpia.controller;


import com.likelion.catdogpia.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final AddressService addressService;

    // 프로필 페이지
    @GetMapping("/mypage")
    public String profilePage(Model model) {
        return "page/mypage/profile.html";
    }

    // 회원 정보 수정 페이지
    @GetMapping("/mypage/edit-profile")
    public String editProfilePage(Model model) {
        return "page/mypage/edit_profile.html";
    }

    // 반려동물 등록 페이지
    @GetMapping("/mypage/edit-pet")
    public String addPetPage(Model model) {
        return "page/mypage/add_pet.html";
    }

    // 반려동물 등록
    @PostMapping("/mypage/edit-pet")
    public String addPet(Model model) {
        return "page/mypage/add_pet.html";
    }

    // 주문 내역 페이지
    @GetMapping("/mypage/order-list")
    public String orderListPage(Model model) {
        return "page/mypage/order_list.html";
    }

    // 주문 상세 페이지
    @GetMapping("/mypage/order-detail")
    public String orderDetailPage(Model model) {
        return "page/mypage/order_detail.html";
    }

    // 교환 및 환불 페이지
    @GetMapping("/mypage/exchange-refund")
    public String exchangeRefundPage(Model model) {
        return "page/mypage/exchange_refund.html";
    }

    // 적립금 페이지
    @GetMapping("/mypage/point")
    public String pointPage(Model model) {
        return "page/mypage/point.html";
    }

    // 배송지 관리 페이지
    @GetMapping("/mypage/address")
    public String addressPage(Model model) {
        // 로그인 여부 확인
        // 배송지 리스트
//        addressService.readAllAddress()
        return "page/mypage/address.html";
    }

    // 배송지 등록 페이지
    @GetMapping("/mypage/add-address")
    public String editAddressPage(Model model) {
        return "page/mypage/add_address.html";
    }

    // 리뷰 관리
    @GetMapping("/mypage/review")
    public String reviewPage(Model model) {
        return "page/mypage/review.html";
    }

    // 게시글 관리 페이지
    @GetMapping("/mypage/article")
    public String mypagePage(Model model) {
        return "page/mypage/article.html";
    }

}
