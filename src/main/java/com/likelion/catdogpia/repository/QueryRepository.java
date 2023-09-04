package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.admin.*;
import com.likelion.catdogpia.domain.entity.attach.QAttach;
import com.likelion.catdogpia.domain.entity.attach.QAttachDetail;
import com.likelion.catdogpia.domain.entity.order.QOrders;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.domain.entity.product.QOrderProduct;
import com.likelion.catdogpia.domain.entity.product.QProductOption;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.likelion.catdogpia.domain.entity.user.QMember.*;
import static com.likelion.catdogpia.domain.entity.product.QProduct.*;
import static com.likelion.catdogpia.domain.entity.product.QOrderProduct.*;
import static com.likelion.catdogpia.domain.entity.order.QOrders.*;
import static com.likelion.catdogpia.domain.entity.community.QArticle.*;
import static com.likelion.catdogpia.domain.entity.report.QReport.*;

// 특정 엔티티 타입에 구애받지 않는 QueryDSL 관련 Repository
@Slf4j
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
                        .where(searchFilter(filter, keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(member.id.desc())
                        .fetch();
        // 카운트
        Long count = queryFactory
                .select(member.count())
                .from(member)
                .where(searchFilter(filter, keyword))
                .fetchOne();

        return new PageImpl<>(memberList, pageable, count);
    }

    // 검색 조건 추가
    private BooleanExpression searchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter)) {
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
                        .where(productSearchFilter(filter, keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(product.id.desc())
                        .fetch();
        // 카운트
        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(productSearchFilter(filter, keyword))
                .fetchOne();

        return new PageImpl<>(productListDtoList, pageable, count);
    }

    // 검색 조건 추가
    private BooleanExpression productSearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter)) {
            return switch (filter) {
                case "name" -> product.name.contains(keyword);
                default -> product.status.eq(keyword);
            };
        } else {
            return null;
        }
    }

    // 상품 목록 조회
    public Page<OrderListDto> findByOrderList(Pageable pageable, String filter, String keyword, String toDate, String fromDate, OrderStatus orderStatus) {
        QOrders orders = new QOrders("orders");
        QOrderProduct orderProduct = new QOrderProduct("orderProduct");
        QProductOption productOption = new QProductOption("productOption");

        List<OrderListDto> orderList =
                queryFactory.select(Projections.fields(OrderListDto.class,
                                orders.id,
                                orders.orderAt,
                                orders.orderNumber,
                                product.name.as("productName"),
                                member.name.as("buyerName"),
                                productOption.color,
                                productOption.size,
                                orderProduct.orderStatus,
                                orderProduct.quantity,
                                orderProduct.id.as("orderProductId")
                        ))
                        .from(orders)
                        .join(member).on(orders.member.eq(member))
                        .join(orderProduct).on(orders.eq(orderProduct.order))
                        .join(productOption).on(orderProduct.productOption.eq(productOption))
                        .join(product).on(productOption.product.eq(product))
                        .where(orderSearchFilter(filter, keyword),
                                orderDateFilter(toDate, fromDate),
                                orderStatusFilter(orderStatus))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(orders.id.desc())
                        .fetch();

        Long count = queryFactory
                .select(orders.count())
                .from(orders)
                .join(member).on(orders.member.eq(member))
                .join(orderProduct).on(orders.eq(orderProduct.order))
                .join(productOption).on(orderProduct.productOption.eq(productOption))
                .join(product).on(productOption.product.eq(product))
                .where(orderSearchFilter(filter, keyword),
                        orderDateFilter(toDate, fromDate),
                        orderStatusFilter(orderStatus))
                .fetchOne();

        return new PageImpl<>(orderList, pageable, count);


    }

    // 검색 조건 추가
    private BooleanExpression orderSearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter)) {
            return switch (filter) {
                case "buyerName" -> member.name.contains(keyword);
                default -> orders.orderNumber.contains(keyword);
            };
        } else {
            return null;
        }
    }

    // 날짜 조건 추가
    private BooleanExpression orderDateFilter(String toDate, String fromDate) {
        if (StringUtils.hasText(toDate) && StringUtils.hasText(fromDate)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate parseToDate = LocalDate.parse(toDate, dateFormatter);
                LocalDate parseFromDate = LocalDate.parse(fromDate, dateFormatter);

                return orders.orderAt.between(parseFromDate.atStartOfDay(), parseToDate.atStartOfDay());
            } catch (DateTimeParseException ex) {
                // 날짜 형식이 잘못된 경우 처리
                log.info("날짜 형식이 잘못됨!");
                ex.printStackTrace();
            }
        } else {
            return null;
        }
        return null;
    }

    // 주문 상태 조건 추가
    private BooleanExpression orderStatusFilter(OrderStatus orderStatus) {
        if (orderStatus != null) {
            return orderProduct.orderStatus.eq(orderStatus);
        } else {
            return null;
        }
    }

    public List<OrderDto> findOrder(Long orderId) {
        QProductOption productOption = new QProductOption("productOption");
        QAttach attach = new QAttach("attach");
        QAttachDetail attachDetail = new QAttachDetail("attachDetail");

        return queryFactory.select(orders)
                .from(orders)
                .join(orderProduct).on(orderProduct.order.eq(orders))
                .join(productOption).on(orderProduct.productOption.eq(productOption))
                .join(product).on(productOption.product.eq(product))
                .leftJoin(attach).on(product.attach.eq(attach))
                .leftJoin(attachDetail).on(attachDetail.attach.eq(attach)
                        .and(attachDetail.id.eq(
                                JPAExpressions.select(attachDetail.id.min())
                                        .from(attachDetail)
                                        .where(attachDetail.attach.eq(attach))
                )))
                .where(orders.id.eq(orderId))
                .transform(GroupBy.groupBy(orders.id).list(
                        Projections.fields(OrderDto.class,
                                orders.id,
                                orders.orderNumber,
                                orders.name.as("buyerName"),
                                orders.address,
                                orders.phone,
                                orders.request,
                                orders.cardCompany,
                                orders.totalAmount,
                                orders.deliveryCharge,
                                orders.discountAmount,
                                orders.orderAt,
                                GroupBy.list(Projections.fields(OrderProductDto.class,
                                        orderProduct.id,
                                        orderProduct.orderStatus,
                                        orderProduct.deliveryAt,
                                        orderProduct.purchaseConfirmedAt,
                                        orderProduct.exchangeRequestedAt,
                                        orderProduct.exchangeCompletedAt,
                                        orderProduct.refundRequestedAt,
                                        orderProduct.refundCompletedAt,
                                        orderProduct.quantity,
                                        product.name.as("productName"),
                                        product.price,
                                        productOption.size,
                                        productOption.color,
                                        attachDetail.fileUrl.as("imgUrl")
                                        )).as("orderProductList")
                )));
    }

    // 커뮤니티 목록
    public Page<CommunityListDto> findByCommunityList(Pageable pageable, String filter, String keyword) {

        List<CommunityListDto> communityList =
                queryFactory.select(Projections.fields(CommunityListDto.class,
                        article.id,
                        article.title,
                        article.member.name.as("writer"),
                        article.viewCnt,
                        article.likeCnt,
                        ExpressionUtils.as(
                            JPAExpressions.select(report.id.count().castToNum(Integer.class))
                                    .from(report)
                                    .where(report.article.id.eq(article.id)),
                                "reportCnt")
                        ))
                        .from(article)
                        .join(member).on(article.member.eq(member))
                        .where(communitySearchFilter(filter, keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(article.id.desc())
                        .fetch();

        Long count =
                queryFactory.select(article.count())
                        .from(article)
                        .join(member).on(article.member.eq(member))
                        .where(communitySearchFilter(filter, keyword))
                        .fetchOne();


        return new PageImpl<>(communityList, pageable, count);
    }

    // 주문 상태 조건 추가
    private BooleanExpression communitySearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter) && StringUtils.hasText(keyword)) {
            return switch (filter) {
                case "number" -> article.id.eq(Long.parseLong(keyword));
                case "title" -> article.title.contains(keyword);
                default -> member.name.contains(keyword);
            };
        } else {
            return null;
        }
    }
}
