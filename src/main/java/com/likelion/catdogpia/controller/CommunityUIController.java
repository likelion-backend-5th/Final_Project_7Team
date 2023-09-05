package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.admin.CategoryDto;
import com.likelion.catdogpia.domain.dto.community.ArticleDto;
import com.likelion.catdogpia.domain.dto.community.CommentDto;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommunityUIController {
    private final CommunityService communityService;
    private Boolean writer;
    private Boolean isWriter;

    //커뮤니티 UI
    @GetMapping("/community")
    public String community() {
        return "page/community/community";
    }

    //글쓰기 UI
    @GetMapping("/community/write")
    public String writingForm(Model model) {
        List<CategoryDto> categoryList = communityService.findCategory();
        model.addAttribute("categoryList", categoryList);
        return "page/community/community-create";
    }

    //글 상세보기
    @GetMapping("/community/{articleId}")
    public String article(@PathVariable Long articleId, Model model) {
        ArticleDto article = communityService.findArticle(articleId);
        List<CategoryDto> categoryList = communityService.findCategory();
        communityService.updateViewCnt(articleId);
        model.addAttribute("article", article);
        model.addAttribute("categoryList", categoryList);
        return "page/community/community-article";
    }

    //글수정 UI
    @GetMapping("/community/{articleId}/edit")
    public String editArticle(@PathVariable Long articleId, Model model) {
        ArticleDto article = communityService.findArticle(articleId);
        model.addAttribute("articleId", articleId);
        model.addAttribute("article", article);
        List<CategoryDto> categoryList = communityService.findCategory();
        model.addAttribute("categoryList", categoryList);
        return "page/community/community-modify";
    }
}
