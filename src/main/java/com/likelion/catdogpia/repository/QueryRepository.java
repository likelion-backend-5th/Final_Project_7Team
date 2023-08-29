package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.admin.MemberListDto;
import com.likelion.catdogpia.domain.dto.admin.ProductListDto;
import com.likelion.catdogpia.domain.entity.product.QProduct;
import com.likelion.catdogpia.domain.entity.product.QProductOption;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.likelion.catdogpia.domain.entity.user.QMember.*;
import static com.likelion.catdogpia.domain.entity.product.QProduct.*;

// 특정 엔티티 타입에 구애받지 않는 QueryDSL 관련 Repository
@RequiredArgsConstructor
@Repository
public class QueryRepository {

    private final JPAQueryFactory queryFactory;

    // 회원관리 목록
    public Page<MemberListDto> findByFilterAndKeyword(Pageable pageable, String filter, String keyword) {
        // 회원 목록
       List<MemberListDto> memberList =
               queryFactory.select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.name,
                        member.loginId,
                        member.nickname,
                        member.blackListYn,
                        member.createdAt
                ))
                .from(member)
                .where(searchFilter(filter,keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.id.desc())
                .fetch();
        // 카운트
        Long count = queryFactory
                .select(member.count())
                .from(member)
                .where(searchFilter(filter,keyword))
                .fetchOne();

        return new PageImpl<>(memberList, pageable, count);
    }

    // 검색 조건 추가
    private BooleanExpression searchFilter(String filter, String keyword) {
        if(StringUtils.hasText(filter)) {
            return switch (filter) {
                case "loginId" -> member.loginId.contains(keyword);
                case "name" -> member.name.contains(keyword);
                case "nickname" -> member.nickname.contains(keyword);
                default -> member.blackListYn.eq(keyword.charAt(0));
            };
        } else {
            return null;
        }
    }

    // 상품 목록
    public Page<ProductListDto> findByProductAndFilterAndKeyword(Pageable pageable, String filter, String keyword) {
        QProductOption qProductOption = new QProductOption("qProductOption");
        List<ProductListDto> productListDtoList =
                queryFactory.select(Projections.fields(ProductListDto.class,
                                product.id,
                                product.name,
                                product.status,
                                product.price,
                                ExpressionUtils.as(
                                        JPAExpressions.select(qProductOption.stock.sum())
                                        .from(qProductOption)
                                        .where(qProductOption.product.eq(product)),
                                "totalStock")
                        ))
                        .from(product)
                        .where(productSearchFilter(filter,keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.id.desc())
                        .fetch();
        // 카운트
        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(productSearchFilter(filter,keyword))
                .fetchOne();

        return new PageImpl<>(productListDtoList, pageable, count);
    }

    // 검색 조건 추가
    private BooleanExpression productSearchFilter(String filter, String keyword) {
        if(StringUtils.hasText(filter)) {
            return switch (filter) {
                case "name" -> product.name.contains(keyword);
                default -> product.status.eq(keyword);
            };
        } else {
            return null;
        }
    }
}
