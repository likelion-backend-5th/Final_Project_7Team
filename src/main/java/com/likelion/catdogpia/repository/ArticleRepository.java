package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.community.ArticleListDto;
import com.likelion.catdogpia.domain.entity.community.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    //조회수 count
    @Modifying
    @Query("update Article p set p.viewCnt = p.viewCnt + 1 where p.id = :id")
    int updateViewCnt(Long id);

    //일주일 이내에 작성된 인기글 중에서 상위 3개만 조회
    @Query("SELECT new com.likelion.catdogpia.domain.dto.community.ArticleListDto(a.id, a.category.id, a.title, a.member, a.attach, a.viewCnt, a.likeCnt, count(c), a.createdAt) FROM Article a LEFT JOIN Comment c ON a.id = c.article.id WHERE a.createdAt >= :oneWeekAgo GROUP BY a.id ORDER BY a.viewCnt DESC limit 3")
    List<ArticleListDto> findTop3PopularArticlesWithinOneWeek(LocalDateTime oneWeekAgo);
}
