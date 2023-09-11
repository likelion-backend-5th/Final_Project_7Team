package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.cart.CartListDto;
import com.likelion.catdogpia.domain.dto.mypage.MemberArticleListDto;
import com.likelion.catdogpia.domain.entity.cart.Cart;
import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.ArticleRepository;
import com.likelion.catdogpia.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberArticleService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    // 게시글 목록 조회
    public Page<MemberArticleListDto> getArticleList(String loginId, Integer page) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<MemberArticleListDto> articleListPage = articleRepository.findByMemberId(pageable, member.getId());

        return articleListPage;
    }

    // 게시물 삭제
    public void deleteArticle(String loginId, List<Long> articleIds) {
        for(Long articleId : articleIds) {
            Article article = articleRepository.findById(articleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            articleRepository.delete(article);
        }
    }

}
