package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.community.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Article, Long> {
}
