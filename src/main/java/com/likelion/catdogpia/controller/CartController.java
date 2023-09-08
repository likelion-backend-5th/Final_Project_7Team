package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 조회
    @GetMapping("")
    public String cartPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Map<String, Object> cartInfo = cartService.readAllCart("testtest", page);
        model.addAttribute("cartList", cartInfo.get("cartList"));
        model.addAttribute("totalAmount", cartInfo.get("totalAmount"));

        return "page/cart.html";
    }

    // 장바구니 수량 변경
    @PutMapping("")
    @ResponseBody
    public int updateCart(@RequestParam Long id, @RequestParam String mp) {
        // 변경된 수량 return
        return cartService.updateCount("testtest", id, mp);
    }

    // 장바구니 상품 삭제
    @DeleteMapping("")
    @ResponseBody
    public String deleteCart(@RequestParam(value = "cartIds[]") List<Long> cartIds) {
        cartService.deleteCart("testtest", cartIds);
        return "success";
    }

}