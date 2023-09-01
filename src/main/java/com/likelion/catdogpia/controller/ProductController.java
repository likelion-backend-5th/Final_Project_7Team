package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.product.ProductListResponseDto;
import com.likelion.catdogpia.domain.dto.product.ProductResponseDto;
import com.likelion.catdogpia.service.CategoryService;
import com.likelion.catdogpia.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    //대분류 조회
    @GetMapping("/{mainCategoryId}")
    public String productList(
            Model model,
            @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable("mainCategoryId") Long mainCategoryId,
            @RequestParam(value = "search", required = false) String searchKeyword
    ) {

        Page<ProductListResponseDto> list = productService.getProductList(pageable, mainCategoryId, searchKeyword);
        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("productList", productService.getProductList(pageable, mainCategoryId, searchKeyword));
        model.addAttribute("categoryList", categoryService.getCategoryList(mainCategoryId));
        model.addAttribute("mainCategory", categoryService.getMainCategory(mainCategoryId));
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "page/product/product-list";
    }

    //소분류 조회
    @GetMapping("/{mainCategoryId}/{categoryId}")
    public String productSmallList(
            Model model,
            @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable("mainCategoryId") Long mainCategoryId,
            @PathVariable(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "search", required = false) String searchKeyword
    ) {
        Page<ProductListResponseDto> list = productService.getProductSearchList(pageable, categoryId, searchKeyword);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

//        model.addAttribute("productList", productService.getProductSearchList(pageable, categoryId, searchKeyword));
        model.addAttribute("productList", list);
        model.addAttribute("categoryList", categoryService.getCategoryList(mainCategoryId));
        model.addAttribute("mainCategory", categoryService.getMainCategory(mainCategoryId));
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("categoryId", categoryId);

        return "page/product/product-list";
    }


    // 상세 조회
    @GetMapping("/detail/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        ProductResponseDto product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "page/product/product-detail";
    }
}