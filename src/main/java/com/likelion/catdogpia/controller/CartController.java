package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final JwtTokenProvider jwtTokenProvider;

    // 장바구니 조회 (페이지)
    @GetMapping("")
    public String cartPage(@RequestParam(value = "page", defaultValue = "0") Integer page) {

        return "page/cart.html";
    }

    // 장바구니 조회 (데이터)
    @GetMapping("/data")
    public Map<String, Object> cartPage(@RequestHeader("Authorization") String accessToken, @RequestParam(value = "page", defaultValue = "0") Integer page) {

        String token = accessToken.split(" ")[1];
        String loginId = jwtTokenProvider.parseClaims(token).getSubject();

        // 장바구니 목록 조회
        Map<String, Object> response = cartService.readAllCart(loginId, page);
//        model.addAttribute("cartList", cartInfo.get("cartList"));
//        model.addAttribute("totalAmount", cartInfo.get("totalAmount"));

        return response;
    }

    // 장바구니 수량 변경
    @PutMapping("")
    @ResponseBody
    public int updateCart(@RequestParam Long id, @RequestParam String mp) {
        // 변경된 수량 return
        return cartService.updateCount(id, mp);
    }

    // 장바구니 상품 삭제
    @DeleteMapping("")
    @ResponseBody
    public String deleteCart(@RequestParam(value = "cartIds[]") List<Long> cartIds) {
        cartService.deleteCart(cartIds);
        return "success";
    }

}
