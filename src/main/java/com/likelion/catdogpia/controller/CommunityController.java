package com.likelion.catdogpia.controller;

import com.likelion.catdogpia.domain.dto.admin.CategoryDto;
import com.likelion.catdogpia.domain.dto.community.ArticleDto;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;
    private final JwtTokenProvider jwtTokenProvider;

    //글쓰기
    @PostMapping("/community/write")
    public Map<String, String> createArticle(@RequestHeader("Authorization") String accessToken, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "images", required = false) List<MultipartFile> images, @RequestParam("selectedCategory") Long selectedCategoryId) throws IOException {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        } else {
            if (images != null) {
                String token = accessToken.split(" ")[1];
                String loginId = jwtTokenProvider.parseClaims(token).getSubject();
                communityService.createArticle(title, content, images, selectedCategoryId, loginId);
                response.put("result", "성공");
            } else {
                String token = accessToken.split(" ")[1];
                String loginId = jwtTokenProvider.parseClaims(token).getSubject();
                communityService.createArticle(title, content, selectedCategoryId, loginId);
                response.put("result", "성공");
            }
        }
        return response;
    }

    //글수정
    @PostMapping("/community/{articleId}/edit")
    public Map<String, String> editArticle(@PathVariable("articleId") Long articleId, @RequestHeader("Authorization")String accessToken, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam(value = "images", required = false) List<MultipartFile> images, @RequestParam("selectedCategory") Long selectedCategoryId) throws IOException {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        } else {
            if (images != null) {
                String token = accessToken.split(" ")[1];
                String loginId = jwtTokenProvider.parseClaims(token).getSubject();
                communityService.updateArticle(articleId, title, content, images, selectedCategoryId, loginId);
                response.put("result", "성공");
            } else {
                String token = accessToken.split(" ")[1];
                String loginId = jwtTokenProvider.parseClaims(token).getSubject();
                communityService.updateArticle(articleId, title, content, selectedCategoryId, loginId);
                response.put("result", "성공");
            }
        }
        return response;
    }

    //이미지 삭제
    @PostMapping("/community/{articleId}/deleteImage")
    public Map<String, String> deleteImage(@PathVariable("articleId") Long articleId, @RequestParam("fileName") String fileName) {
        Map<String, String> response = new HashMap<>();
        log.info(fileName);
        // 이미지 삭제 로직 추가
        try {
            communityService.deleteImage(articleId, fileName);
            response.put("result", "성공");
        } catch (Exception e) {
            response.put("result", "실패");
        }

        return response;
    }

    //글삭제
    @PostMapping("/community/{articleId}/delete")
    public Map<String, String> deleteArticle(@PathVariable("articleId") Long articleId) {
        Map<String, String> response = new HashMap<>();
        communityService.deleteArticle(articleId);
        response.put("result", "성공");
        return response;
    }

    //댓글쓰기
    @PostMapping("/community/{articleId}/writeComment")
    public Map<String, String> createComment(@PathVariable("articleId") Long articleId, @RequestHeader("Authorization")String accessToken, @RequestParam("content") String content) {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        } else {
            String token = accessToken.split(" ")[1];
            String loginId = jwtTokenProvider.parseClaims(token).getSubject();
            communityService.createComment(articleId, loginId, content);
            response.put("result", "성공");
        }
        return response;
    }

    //댓글수정
    @PostMapping("/community/editComment")
    public Map<String, String> updateComment(@RequestParam("commentId")Long commendId, @RequestParam("content") String content) {
        Map<String, String> response = new HashMap<>();
        log.info(String.valueOf(commendId));
        communityService.updateComment(commendId, content);
        response.put("result", "성공");
        return response;
    }

    //댓글삭제
    @PostMapping("/community/deleteComment")
    public Map<String, String> deleteComment(@RequestParam("commentId")Long commendId) {
        Map<String, String> response = new HashMap<>();
        log.info(String.valueOf(commendId));
        communityService.deleteComment(commendId);
        response.put("result", "성공");
        return response;
    }

    //좋아요/취소
    @PostMapping("/community/{articleId}/like")
    public Map<String, String> likeAndUnlike(@PathVariable("articleId")Long articleId, @RequestHeader("Authorization")String accessToken) {
        Map<String, String> response = new HashMap<>();
        if (accessToken == null) {
            response.put("result", "실패");
        } else {
            String token = accessToken.split(" ")[1];
            String loginId = jwtTokenProvider.parseClaims(token).getSubject();
            String result = communityService.likeUnlike(articleId, loginId);
            response.put("result", result);
        }
        return response;
    }
}