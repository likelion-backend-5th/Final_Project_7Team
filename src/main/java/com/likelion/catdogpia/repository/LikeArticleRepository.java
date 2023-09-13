package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {
    Optional<LikeArticle> findByArticle_IdAndMember_LoginId(Long articleId, String loginId);

    List<LikeArticle> findByArticle_Id(Long articleId);
}
