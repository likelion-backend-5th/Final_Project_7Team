package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final CartService cartService;

    @GetMapping("/")
    public String home() {
        return "page/index.html";
    }

    // 장바구니 조회
    @GetMapping("/cart")
    public String cartPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Map<String, Object> cartInfo = cartService.readAllCart("testtest", page);
        model.addAttribute("cartList", cartInfo.get("cartList"));
        model.addAttribute("totalAmount", cartInfo.get("totalAmount"));

        return "page/cart.html";
    }

    // 장바구니 수량 변경
    @PutMapping("/cart")
    @ResponseBody
    public int updateCart(Model model, @RequestParam Long id, @RequestParam String mp) {
        return cartService.updateCount("testtest", id, mp);
    }

    // 장바구니 상품 삭제
    @PostMapping("/cart/delete")
    public String deleteCart() {
        return "redirect:/cart";
    }


}
