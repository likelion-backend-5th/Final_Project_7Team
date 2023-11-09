package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.community.HotArticleListDto;
import com.likelion.catdogpia.service.CommunityService;
import com.likelion.catdogpia.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final CommunityService communityService;
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model) {

        // 인기 상품
        model.addAttribute("hotProductList", productService.getHotProductList());

        // 신상품 리스트 (전체)
        model.addAttribute("newProductList", productService.getNewProductList());

        // 인기 커뮤니티글 리스트
        List<HotArticleListDto> list = communityService.findPopularArticlesWithinOneWeek();

        for (HotArticleListDto hotArticleListDto : list) {
            log.info("list : " + hotArticleListDto.toString());
        }
        model.addAttribute("hotArticleList", communityService.findPopularArticlesWithinOneWeek());



        return "page/index";
    }

}
