package com.likelion.catdogpia.controller;


import com.likelion.catdogpia.domain.dto.mypage.*;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public Map<String, Object> updateProfilePage(@RequestHeader("Authorization") String accessToken) {

        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        MemberProfileDto memberProfileDto = profileService.getMemberProfile(loginId);

        // 회원 정보
        response.put("profile", memberProfileDto);

        return response;

    }

    // 회원 정보 수정 요청
    @PutMapping("/profile/update/{loginId}")
    public ResponseDto profilePost(@PathVariable String loginId, @ModelAttribute MemberModifyFormDto dto) {

        profileService.updateProfile(loginId, dto);

        return new ResponseDto("success");
    }

    // 반려동물 등록 요청
    @PostMapping("/pet")
    public ResponseDto petPost(PetFormDto petFormDto) {
        petService.savePet("testtest", petFormDto);
        return new ResponseDto("success");
    }

    // 주문 내역 페이지
    @GetMapping("/order-list/data")
    public Map<String, Object>  orderListPage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "orderStatus", required = false) OrderStatus orderStatus, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        Page<OrderListDto> orderList = orderHistoryService.getOrderList(loginId, orderStatus, page);

        response.put("orderList", orderList);
        response.put("orderStatusCount", orderHistoryService.getOrderCountByStatus(loginId));

        return response;
    }

    // 주문 내역 > 구매 확정
    @PutMapping("/order-list/purchase-confirm/{opId}")
    public ResponseDto purchaseConfirm(@PathVariable Long opId) {
        orderHistoryService.purchaseConfirm(opId);
        return new ResponseDto("구매 확정 되었습니다.");
    }

    // 주문 내역 > 리뷰 작성 페이지
    @GetMapping("/order-list/review/{opId}/data")
    public Map<String, Object> reviewPage(@PathVariable Long opId, @RequestHeader("Authorization") String accessToken) {

        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        ReviewProductDto orderProudct = reviewService.getOrderProduct(loginId, opId);

        response.put("orderProudct", orderProudct);

        return response;
    }

    // 리뷰 등록 요청
    @PostMapping("/order-list/review/{opId}")
    public ResponseDto reviewPost(@PathVariable Long opId, @RequestPart(name = "reviewImg", required = false) MultipartFile reviewImg, @Valid @RequestPart("reviewFormDto") ReviewFormDto reviewFormDto) throws IOException {
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//        log.info("로그인id => " + loginId);

        reviewService.saveReview(opId, reviewImg, reviewFormDto);
        return new ResponseDto("success");
    }

    // 교환 요청 페이지
//    @GetMapping("/order-list/exchange/{opId}/data")
//    public Map<String, Object> exchangePage(@PathVariable Long opId, @RequestHeader("Authorization") String accessToken) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//
//        ExchangeRefundDto exchangeRefundDto = orderHistoryService.getOrderInfo(loginId, opId);
//        Map<String, List<String>> map = orderHistoryService.getProductOption(opId);
//
//        response.put("order", exchangeRefundDto);
//        response.put("map", map);
//
//        return response;
//    }

    // 교환 요청 처리
    @PostMapping("/order-list/exchange")
    public ResponseDto exchangePost(@RequestBody ExchangeRequestDto dto) {
        orderHistoryService.exchange(dto);
        return new ResponseDto("success");
    }

//    // 환불 요청 페이지
//    @GetMapping("/order-list/refund/{opId}/data")
//    public Map<String, Object> refundPage(@PathVariable Long opId, @RequestHeader("Authorization") String accessToken) {
////        model.addAttribute("order", orderHistoryService.getOrderInfo("testtest", opId));
//        Map<String, Object> response = new HashMap<>();
//
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//
////        response.put();
//
//        return response;
//    }

    // 환불 요청 처리
    @PostMapping("/order-list/refund")
    public ResponseDto refundPost(@RequestBody RefundRequestDto dto) {
        orderHistoryService.refund(dto);
        return new ResponseDto("success");
    }

    // 주문 상세 페이지
//    @GetMapping("/order-detail/{orderId}/data")
//    public Map<String, Object> orderDetailPage(@PathVariable Long orderId, @RequestHeader("Authorization") String accessToken, @RequestParam(value = "page", defaultValue = "0") Integer page) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//
//        Page<OrderListDto> productList = orderHistoryService.getOrder(loginId, orderId, page);
//        OrderDetailDto productDetail = orderHistoryService.getDetail(loginId, orderId);
//
//        response.put("productList", productList);
//        response.put("productDetail", productDetail);
//
//        return response;
//    }

    // 적립금 페이지
    @GetMapping("/point/data")
    public Map<String, Object> pointPage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        Page<PointDto> pointList = pointService.getPointList(loginId, page);

        response.put("pointList", pointList);

        return response;
    }

    // 배송지 관리 페이지
    @GetMapping("/address/data")
    public Map<String, Object> addressPage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        Page<AddressListDto> addressList = addressService.readAllAddress(loginId, page);

        log.info("배송지총개수:" + addressList.getTotalElements());

        response.put("addressList", addressList);

        return response;
    }

    // 배송지 등록 요청
    @PostMapping("/add-address")
    public String postAddAddress(@RequestHeader("Authorization") String accessToken, AddressFormDto dto) {
        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
        addressService.saveAddress(loginId, dto);
        return "redirect:/mypage/address";
    }

    // 배송지 수정 팝업 페이지
//    @GetMapping("/address/update/{addressId}/data")
//    public Map<String, Object> updateAddressPage(@PathVariable Long addressId) {
////        model.addAttribute("address", addressService.readAddress(addressId));
//        Map<String, Object> response = new HashMap<>();
//
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//
////        response.put();
//
//        return response;
//    }

    // 배송지 수정 요청
    @PostMapping("/address/update/{addressId}")
    public String updateAddress(@PathVariable Long addressId, AddressFormDto dto) {
        addressService.updateAddress(addressId, dto);
        return "redirect:/mypage/address";
    }
    
    // 배송지 삭제 요청
    @PostMapping("/address/delete/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return "redirect:/mypage/address";
    }

    // 리뷰 관리 페이지
    @GetMapping("/review/data")
    public Map<String, Object> reviewPage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "page", defaultValue = "0") Integer page) {

        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        Page<ReviewListDto> reviewList = reviewService.getReviewList(loginId, page);

        response.put("reviewList", reviewList);

        return response;
    }

    // 리뷰 수정 페이지
//    @GetMapping("/review/{reviewId}/data")
//    public Map<String, Object> reviewModifyPage(@PathVariable Long reviewId, @RequestHeader("Authorization") String accessToken) {
////        model.addAttribute("review", reviewService.getReview("testtest", reviewId));
//        Map<String, Object> response = new HashMap<>();
//
//        String token = accessToken.split(" ")[1];
//        String loginId = jwtTokenProvider.parseClaims(token).getSubject();
//
////        response.put();
//
//        return response;
//    }

    // 리뷰 수정 요청
    @PutMapping("/review/{reviewId}")
    public ResponseDto modifyReview(@PathVariable Long reviewId,  @RequestPart(name = "reviewImg", required = false) MultipartFile reviewImg, @Valid @RequestPart("reviewFormDto") ReviewFormDto reviewFormDto) throws IOException {
        reviewService.updateReview(reviewId, reviewImg, reviewFormDto);
        return new ResponseDto("success");
    }

    // 리뷰 삭제 요청
    @DeleteMapping("/review/{reviewId}")
    public ResponseDto deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return new ResponseDto("success");
    }

    // 게시글 관리 페이지
    @GetMapping("/article/data")
    public Map<String, Object> articlePage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "page", defaultValue = "0") Integer page) {

        Map<String, Object> response = new HashMap<>();

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        Page<MemberArticleListDto> articleList = memberArticleService.getArticleList(loginId, page);

        response.put("articleList", articleList);

        return response;
    }

    // 게시글 삭제
    @DeleteMapping("/article")
    public ResponseDto deleteArticle(@RequestParam("articleIds[]") List<Long> articleIds) {
        memberArticleService.deleteArticle(articleIds);
        return new ResponseDto("success");
    }

}
