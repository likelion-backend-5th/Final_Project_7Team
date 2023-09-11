package com.likelion.catdogpia.controller;


import com.likelion.catdogpia.domain.dto.mypage.*;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final AddressService addressService;
    private final OrderHistoryService orderHistoryService;
    private final PointService pointService;
    private final ReviewService reviewService;
    private final PetService petService;
    private final ProfileService profileService;
    private final MemberArticleService memberArticleService;

    private final JwtTokenProvider jwtTokenProvider;

    // 프로필 페이지
    @GetMapping("/profile/data")
    public Map<String, Object> profilePage(@RequestHeader("Authorization") String accessToken) {

        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        MemberProfileDto dto = profileService.getMemberProfile(loginId);

        // 회원 정보
        response.put("profile", dto);

        return response;
    }

    // 회원 정보 수정 페이지
    @GetMapping("/profile/update/data")
    public String updateProfilePage(Model model) {

        model.addAttribute("profile", profileService.getMemberProfile("testtest"));
        return "page/mypage/profile_modify.html";
    }

    // 회원 정보 수정 요청
    @PutMapping("/profile/update")
    public ResponseDto profilePost(@ModelAttribute MemberModifyFormDto dto) {
        profileService.updateProfile("testtest", dto);

        return new ResponseDto("success");
    }

    // 반려동물 등록 요청
    @PostMapping("/pet")
    public ResponseDto petPost(PetFormDto petFormDto) {
        petService.savePet("testtest", petFormDto);
        return new ResponseDto("success");
    }

    // 주문 내역 페이지
//    @GetMapping("/order-list/data")
//    public String orderListPage(@RequestHeader("Authorization") String accessToken, Model model, @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page) {
//        Page<OrderListDto> orderList = orderHistoryService.readAllOrder("testtest", orderStatus, page);
//        model.addAttribute("orderList", orderList);
//        // 주문 상태별 개수
//        model.addAttribute("orderStatusCount", orderHistoryService.getOrderCountByStatus("testtest"));
//        return "page/mypage/order_list.html";
//    }
    @GetMapping("/order-list/data")
    public Map<String, Object>  orderListPage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        Page<OrderListDto> orderList = orderHistoryService.readAllOrder(loginId, orderStatus, page);

        response.put("orderList", orderList);
        response.put("orderStatusCount", orderHistoryService.getOrderCountByStatus(loginId));

        return response;
    }

    // 주문 내역 > 구매 확정
    @PutMapping("/order-list/purchase-confirm/{opId}")
    public ResponseDto purchaseConfirm(@PathVariable Long opId) {
        orderHistoryService.purchaseConfirm("testtest", opId);
        return new ResponseDto("구매 확정 되었습니다.");
    }

    // 주문 내역 > 리뷰 작성 페이지
    @GetMapping("/order-list/review/{opId}/data")
    public String reviewPage(@PathVariable Long opId, Model model) {
        model.addAttribute("orderProduct", reviewService.getOrderProduct("testtest", opId));
        return "page/mypage/review_write.html";
    }

    // 리뷰 등록 요청
    @PostMapping("/order-list/review/{opId}")
    public ResponseDto reviewPost(@PathVariable Long opId, @RequestPart(name = "reviewImg", required = false) MultipartFile reviewImg, @Valid @RequestPart("reviewFormDto") ReviewFormDto reviewFormDto) throws IOException {
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//        log.info("로그인id => " + loginId);

        reviewService.saveReview("testtest", opId, reviewImg, reviewFormDto);
        return new ResponseDto("success");
    }

    // 교환 요청 페이지
    @GetMapping("/order-list/exchange/{opId}/data")
    public String exchangePage(@PathVariable Long opId, Model model) {
        model.addAttribute("order", orderHistoryService.getOrderInfo("testtest", opId));
        model.addAttribute("option", orderHistoryService.getProductOption(opId));
        return "page/mypage/exchange.html";
    }

    // 교환 요청 처리
    @PostMapping("/order-list/exchange")
    public ResponseDto exchangePost(@RequestBody ExchangeRequestDto dto) {
        orderHistoryService.exchange("testtest", dto);
        return new ResponseDto("success");
    }

    // 환불 요청 페이지
    @GetMapping("/order-list/refund/{opId}/data")
    public String refundPage(@PathVariable Long opId, Model model) {
        model.addAttribute("order", orderHistoryService.getOrderInfo("testtest", opId));
        return "page/mypage/refund.html";
    }

    // 환불 요청 처리
    @PostMapping("/order-list/refund")
    public ResponseDto refundPost(@RequestBody RefundRequestDto dto) {
        orderHistoryService.refund("testtest", dto);
        return new ResponseDto("success");
    }

    // 주문 상세 페이지
    @GetMapping("/order-detail/{orderId}/data")
    public String orderDetailPage(Model model, @PathVariable Long orderId, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("productList", orderHistoryService.readOrder("testtest", orderId, page, limit));
        model.addAttribute("productDetail", orderHistoryService.readDetail("testtest", orderId));
        return "page/mypage/order_detail.html";
    }

    // 적립금 페이지
    @GetMapping("/point/data")
    public String pointPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        // 적립금 내역
        model.addAttribute("pointList", pointService.findAllPoint("testtest", page));
        return "page/mypage/point.html";
    }

    // 배송지 관리 페이지
    @GetMapping("/address/data")
    public String addressPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        model.addAttribute("addressList", addressService.readAllAddress("testtest", page, limit));
        return "page/mypage/address_list.html";
    }

    // 배송지 등록 요청
    @PostMapping("/add-address")
    public String postAddAddress(AddressFormDto dto) {
        addressService.saveAddress("testtest", dto);
        return "redirect:/mypage/address";
    }

    // 배송지 수정 팝업 페이지
    @GetMapping("/address/update/{addressId}/data")
    public String updateAddressPage(@PathVariable Long addressId, Model model) {
        model.addAttribute("address", addressService.readAddress(addressId));
        return "page/mypage/address_modify.html";
    }

    // 배송지 수정 요청
    @PostMapping("/address/update/{addressId}")
    public String updateAddress(@PathVariable Long addressId, AddressFormDto dto) {
        addressService.updateAddress(addressId, dto);
        return "redirect:/mypage/address";
    }
    
    // 배송지 삭제 요청
    @PostMapping("/address/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress("testtest", addressId);
        return "redirect:/mypage/address";
    }

    // 리뷰 관리 페이지
    @GetMapping("/review/data")
    public String reviewPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        model.addAttribute("reviewList", reviewService.findAllReview("testtest", page));
        return "page/mypage/review_list.html";
    }

    // 리뷰 수정 페이지
    @GetMapping("/review/{reviewId}/data")
    public String reviewModifyPage(@PathVariable Long reviewId, Model model) {
        model.addAttribute("review", reviewService.getReview("testtest", reviewId));
        return "page/mypage/review_modify.html";
    }

    // 리뷰 수정 요청
    @PutMapping("/review/{reviewId}")
    public ResponseDto modifyReview(@PathVariable Long reviewId,  @RequestPart(name = "reviewImg", required = false) MultipartFile reviewImg, @Valid @RequestPart("reviewFormDto") ReviewFormDto reviewFormDto) throws IOException {
        reviewService.updateReview("testtest", reviewId, reviewImg, reviewFormDto);
        return new ResponseDto("success");
    }

    // 리뷰 삭제 요청
    @DeleteMapping("/review/{reviewId}")
    public ResponseDto deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview("testtest", reviewId);
        return new ResponseDto("success");
    }

    // 게시글 관리 페이지
    @GetMapping("/article/data")
    public String mypagePage(Model model) {
        return "page/mypage/article_list.html";
    }

    // 게시글 삭제
    @DeleteMapping("/article")
    public ResponseDto deleteArticle(@RequestParam("articleIds[]") List<Long> articleIds) {
        memberArticleService.deleteArticle("testtest", articleIds);
        return new ResponseDto("success");
    }

}
