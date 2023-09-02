package com.likelion.catdogpia.controller;


import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.domain.dto.mypage.OrderDetailDto;
import com.likelion.catdogpia.domain.dto.mypage.OrderListDto;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.service.AddressService;
import com.likelion.catdogpia.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String orderListPage(Model model, @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Page<OrderListDto> orderList = orderHistoryService.readAllOrder("testtest", orderStatus, page);
        model.addAttribute("orderList", orderList);
        // 주문 상태별 개수
        model.addAttribute("orderStatusCount", orderHistoryService.getOrderCountByStatus("testtest"));
        return "page/mypage/order_list.html";
    }

//    // 주문 내역 페이지 - 주문 상태 필터링
//    @PostMapping("/order-list")
//    public Map<String, Object> orderListPage(OrderStatus orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page) {
//        Map<String, Object> response = new HashMap<>();
//
//        Page<OrderListDto> orderList = orderHistoryService.readAllOrder("testtest", orderStatus, page);
//        response.put("orderList", orderList);
//        // 주문 상태별 개수
//        Map<String, Integer> orderStatusCount = orderHistoryService.getOrderCountByStatus("testtest");
//        response.put("orderStatusCount", orderStatusCount);
//        return response;
//    }

    // 주문 상세 페이지
    @GetMapping("/order-detail/{orderId}")
    public String orderDetailPage(Model model, @PathVariable Long orderId, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("productList", orderHistoryService.readOrder("testtest", orderId, page, limit));
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
