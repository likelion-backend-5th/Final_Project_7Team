package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.product.ProductResponseDto;
import com.likelion.catdogpia.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 테스트 조회
    @GetMapping("/test")
    public String test(){
        return "page/product/product-test";
    }

    //대분류 조회
    @GetMapping("")
    public String productList(
            Model model,
            @PageableDefault(page = 0, size = 21, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, value = "categoryId") Long categoryId
    ) {
        model.addAttribute("productList", productService.getProductList(pageable, categoryId));
        model.addAttribute("categoryList", productService.getCategoryList(categoryId));

        return "page/product/product-list";
    }

    //소분류 조회
    @GetMapping("/category")
    public String productSmallList(
            Model model,
            @PageableDefault(page = 0, size = 21, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, value = "categoryId") Long categoryId,
            @RequestParam(required = false, value = "productName") String productName
    ) {
        model.addAttribute("productList", productService.getProductSearchList(pageable, categoryId, productName));
//        model.addAttribute("productList", productService.getProductSmallList(pageable, categoryId));
        model.addAttribute("categoryList", productService.getCategoryList(categoryId));

        return "page/product/product-list";
    }


    // 상세 조회
    @GetMapping("/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        ProductResponseDto product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "page/product/product-detail";
    }
}