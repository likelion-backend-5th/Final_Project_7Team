package com.likelion.catdogpia.controller;


import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.domain.dto.mypage.OrderDetailDto;
import com.likelion.catdogpia.domain.dto.mypage.OrderListDto;
import com.likelion.catdogpia.service.AddressService;
import com.likelion.catdogpia.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final AddressService addressService;
    private final OrderHistoryService orderHistoryService;

    // 프로필 페이지
    @GetMapping("")
    public String profilePage(Model model) {
        return "page/mypage/profile.html";
    }

    // 회원 정보 수정 페이지
    @GetMapping("/edit-profile")
    public String editProfilePage(Model model) {
        return "page/mypage/edit_profile.html";
    }

    // 반려동물 등록 페이지
    @GetMapping("/edit-pet")
    public String addPetPage(Model model) {
        return "page/mypage/add_pet.html";
    }

    // 반려동물 등록
    @PostMapping("/edit-pet")
    public String addPet(Model model) {
        return "page/mypage/add_pet.html";
    }

    // 주문 내역 페이지
    @GetMapping("/order-list")
    public String orderListPage(Model model, @RequestParam(value = "orderStatus", required = false) String orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        Page<OrderListDto> orderList = orderHistoryService.readAllOrder("testtest", orderStatus, page, limit);
//        List<OrderListDto> contentList = orderList.getContent();
//        OrderListDto firstOrder = contentList.get(0);
//        log.info("테스트1 id : " + firstOrder.getId());
//        log.info("테스트1 name : " + firstOrder.getName());
//        log.info("테스트1 price : " + firstOrder.getPrice());
//        log.info("테스트1 orderAt : " + firstOrder.getOrderAt());
//        log.info("테스트1 quantity : " + firstOrder.getQuantity());
//        log.info("테스트1 size : " + firstOrder.getSize());
//        log.info("테스트1 color : " + firstOrder.getColor());
//        OrderListDto secondOrder = contentList.get(1);
//        log.info("테스트2 id : " + secondOrder.getId());
//        log.info("테스트2 name : " + secondOrder.getName());
//        log.info("테스트2 price : " + secondOrder.getPrice());
//        log.info("테스트2 orderAt : " + secondOrder.getOrderAt());
//        log.info("테스트2 quantity : " + secondOrder.getQuantity());
//        log.info("테스트2 size : " + secondOrder.getSize());
//        log.info("테스트2 color : " + secondOrder.getColor());

        model.addAttribute("orderList", orderList);
        return "page/mypage/order_list.html";
    }

    // 주문 상세 페이지
    @GetMapping("/order-detail/{orderId}")
    public String orderDetailPage(Model model, @PathVariable Long orderId, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("productList", orderHistoryService.readOrder("testtest", orderId, page, limit));
        // 테스트용 OrderDetailDto
        model.addAttribute("testDetail", OrderDetailDto.builder().address("test주소").name("어금지").phone("01011111111").request("문 앞에 두고 가주세요!").totalAmount(30000).cardCompany("비씨카드").point(5000).build());
        model.addAttribute("productDetail", orderHistoryService.readDetail("testtest", orderId));
        return "page/mypage/order_detail.html";
    }

    // 교환 및 환불 페이지
    @GetMapping("/exchange-refund")
    public String exchangeRefundPage(Model model) {
        return "page/mypage/exchange_refund.html";
    }

    // 적립금 페이지
    @GetMapping("/point")
    public String pointPage(Model model) {
        return "page/mypage/point.html";
    }

    // 배송지 관리 페이지
    @GetMapping("/address")
    public String addressPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("addressList", addressService.readAllAddress("testtest", page, limit));
        return "page/mypage/address.html";
    }

    // 배송지 등록 페이지
    @GetMapping("/add-address")
    public String addAddressPage(Model model) {
        return "page/mypage/add_address.html";
    }

    // 배송지 등록
    @PostMapping("/add-address")
    public String postAddAddress(AddressFormDto dto) {
        addressService.saveAddress("testtest", dto);
        return "redirect:/mypage/address";
    }

    // 배송지 수정 팝업 페이지
    @GetMapping("/address/update/{addressId}")
    public String updateAddressPage(@PathVariable Long addressId, Model model) {
        model.addAttribute("address", addressService.readAddress(addressId));
        return "page/mypage/edit_address.html";
    }

    // 배송지 수정
    @PostMapping("/address/update/{addressId}")
    public String updateAddress(@PathVariable Long addressId, AddressFormDto dto) {
        addressService.updateAddress(addressId, dto);
        return "redirect:/mypage/address";
    }

    @PostMapping("/address/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        log.info("delete addressId값 = " + addressId);
        addressService.deleteAddress("testtest", addressId);
        return "redirect:/mypage/address";
    }

    // 리뷰 관리
    @GetMapping("/review")
    public String reviewPage(Model model) {
        return "page/mypage/review.html";
    }

    // 게시글 관리 페이지
    @GetMapping("/article")
    public String mypagePage(Model model) {
        return "page/mypage/article.html";
    }

}
