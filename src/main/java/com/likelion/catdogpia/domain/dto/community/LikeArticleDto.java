package com.likelion.catdogpia.domain.dto.community;

import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.LikeArticle;
import com.likelion.catdogpia.domain.entity.user.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class LikeArticleDto {
    private String nickname;

    //== Entity -> DTO ==//
    public static LikeArticleDto fromEntity(LikeArticle likeArticle) {
        return LikeArticleDto.builder()
                .nickname(likeArticle.getMember().getNickname())
                .build();
    }

    @Builder
    public LikeArticleDto(String nickname) {
        this.nickname = nickname;
    }
}
