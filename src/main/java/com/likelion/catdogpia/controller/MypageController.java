package com.likelion.catdogpia.controller;


import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public String addressPage(Model model, @RequestParam(value="page", defaultValue = "0") Integer page, @RequestParam(value="limit", defaultValue = "10") Integer limit) {
        model.addAttribute("addressList", addressService.readAllAddress("testtest", page, limit));
        return "page/mypage/address.html";
    }

    // 배송지 등록 페이지
    @GetMapping("/mypage/add-address")
    public String addAddressPage(Model model) {
        return "page/mypage/add_address.html";
    }

    // 배송지 등록
    @PostMapping("/mypage/add-address")
    public String postAddAddress(AddressFormDto dto) {
        addressService.saveAddress("testtest", dto);
        return "redirect:/mypage/address";
    }

    // 배송지 수정 팝업 페이지
    @GetMapping("/mypage/address/update/{addressId}")
    public String updateAddressPage(@PathVariable Long addressId, Model model) {
        model.addAttribute("address", addressService.readAddress(addressId));
        return "page/mypage/edit_address.html";
    }

    // 배송지 수정
    @PostMapping("/mypage/address/update/{addressId}")
    public String updateAddress(@PathVariable Long addressId, AddressFormDto dto) {
        addressService.updateAddress(addressId, dto);
        return "redirect:/mypage/address";
    }

    @PostMapping("/mypage/address/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        log.info("delete addressId값 = " + addressId);
        addressService.deleteAddress("testtest", addressId);
        return "redirect:/mypage/address";
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
