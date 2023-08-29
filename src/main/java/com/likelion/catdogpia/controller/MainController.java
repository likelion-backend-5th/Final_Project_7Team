package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

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
    @PostMapping("/cart/update")
    public String updateCart() {
        return "redirect:/cart";
    }

    // 장바구니 상품 삭제
    @PostMapping("/cart/delete")
    public String deleteCart() {
        return "redirect:/cart";
    }


}
