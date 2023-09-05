package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.community.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Modifying
    @Query("update Article p set p.viewCnt = p.viewCnt + 1 where p.id = :id")
    int updateViewCnt(Long id);
}
