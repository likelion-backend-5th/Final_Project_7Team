package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.admin.AttachDetailDto;
import com.likelion.catdogpia.domain.dto.admin.MemberListDto;
import com.likelion.catdogpia.domain.dto.admin.ProductListDto;
import com.likelion.catdogpia.domain.dto.community.ArticleListDto;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.attach.QAttach;
import com.likelion.catdogpia.domain.entity.attach.QAttachDetail;
import com.likelion.catdogpia.domain.entity.community.QArticle;
import com.likelion.catdogpia.domain.entity.community.QComment;
import com.likelion.catdogpia.domain.entity.community.QLikeArticle;
import com.likelion.catdogpia.domain.entity.product.QProduct;
import com.likelion.catdogpia.domain.entity.product.QProductOption;
import com.likelion.catdogpia.domain.dto.admin.*;
import com.likelion.catdogpia.domain.entity.attach.QAttach;
import com.likelion.catdogpia.domain.entity.attach.QAttachDetail;
import com.likelion.catdogpia.domain.entity.community.QComment;
import com.likelion.catdogpia.domain.entity.consultation.ConsulClassification;
import com.likelion.catdogpia.domain.entity.order.QOrders;
import com.likelion.catdogpia.domain.entity.product.*;
import com.likelion.catdogpia.domain.entity.report.QReport;
import com.likelion.catdogpia.domain.entity.review.QReview;
import com.likelion.catdogpia.domain.entity.user.QMember;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.criteria.Subquery;
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

import static com.likelion.catdogpia.domain.entity.community.QArticle.article;
import static com.likelion.catdogpia.domain.entity.user.QMember.*;
import static com.likelion.catdogpia.domain.entity.product.QProduct.*;
import static com.likelion.catdogpia.domain.entity.product.QOrderProduct.*;
import static com.likelion.catdogpia.domain.entity.order.QOrders.*;
import static com.likelion.catdogpia.domain.entity.community.QArticle.*;
import static com.likelion.catdogpia.domain.entity.report.QReport.*;
import static com.likelion.catdogpia.domain.entity.product.QQnA.*;
import static com.likelion.catdogpia.domain.entity.consultation.QConsultation.*;
import static com.likelion.catdogpia.domain.entity.consultation.QConsultationAnswer.*;
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
                default -> product.status.eq(ProductStatus.valueOf(keyword));
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
                LocalDate parseToDate = LocalDate.parse(toDate, dateFormatter).plusDays(1);
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

    // 주문 내역 목록
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
        QLikeArticle likeArticle = QLikeArticle.likeArticle;

        List<CommunityListDto> communityList =
                queryFactory.select(Projections.fields(CommunityListDto.class,
                                article.id,
                                article.title,
                                article.member.name.as("writer"),
                                article.viewCnt,
                                Expressions.as(
                                        JPAExpressions
                                                .select(likeArticle.count())
                                                .from(likeArticle)
                                                .where(likeArticle.article.eq(article)),
                                        "likeCnt"
                                ),
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

    // 상품 QnA관리 목록
    public Page<QnaListDto> findByQnaList(Pageable pageable, String filter, String keyword, String toDate, String fromDate) {
        QQnAAnswer qnAAnswer = new QQnAAnswer("qnAAnswer");
        QMember answerer = new QMember("answerer");
        List<QnaListDto> list =
                queryFactory.select(Projections.fields(QnaListDto.class,
                                qnA.id,
                                qnA.classification,
                                product.name.as("productName"),
                                qnA.title,
                                qnA.member.name.as("writer"),
                                qnA.createdAt,
                                answerer.name.as("answerer"),
                                qnAAnswer.createdAt.as("answeredAt")))
                        .from(qnA)
                        .leftJoin(qnAAnswer).on(qnAAnswer.qna.eq(qnA))
                        .join(product).on(qnA.product.eq(product))
                        .join(member).on(qnA.member.eq(member))
                        .leftJoin(answerer).on(qnAAnswer.member.eq(answerer))
                        .where(qnaSearchFilter(filter, keyword),
                                qnaDateFilter(toDate, fromDate))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(qnA.id.desc())
                        .fetch();

        Long count =
                queryFactory.select(qnA.count())
                        .from(qnA)
                        .leftJoin(qnAAnswer).on(qnAAnswer.qna.eq(qnA))
                        .join(product).on(qnA.product.eq(product))
                        .join(member).on(qnA.member.eq(member))
                        .leftJoin(answerer).on(qnAAnswer.member.eq(answerer))
                        .where(qnaSearchFilter(filter, keyword),
                                qnaDateFilter(toDate, fromDate))
                        .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    // QnA 조건 추가
    private BooleanExpression qnaSearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter) && StringUtils.hasText(keyword)) {
            return switch (filter) {
                case "title" -> qnA.title.contains(keyword);
                case "writer" -> qnA.member.name.contains(keyword);
                case "classification" -> qnA.classification.eq(QnAClassification.valueOf(keyword));
                default -> qnA.product.name.contains(keyword);
            };
        } else {
            return null;
        }
    }

    // 날짜 조건 추가
    private BooleanExpression qnaDateFilter(String toDate, String fromDate) {
        if (StringUtils.hasText(toDate) && StringUtils.hasText(fromDate)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate parseToDate = LocalDate.parse(toDate, dateFormatter).plusDays(1);
                LocalDate parseFromDate = LocalDate.parse(fromDate, dateFormatter);

                return qnA.createdAt.between(parseFromDate.atStartOfDay(), parseToDate.atStartOfDay());
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

    // 1:1 문의 목록
    public Page<ConsultationListDto> findByConsultationList(Pageable pageable, String filter, String keyword, String toDate, String fromDate) {
        QMember answerer = new QMember("answerer");
        List<ConsultationListDto> list =
                queryFactory.select(Projections.fields(ConsultationListDto.class,
                                consultation.id,
                                consultation.classification,
                                consultation.orders.orderNumber,
                                consultation.subject,
                                consultation.member.name.as("writer"),
                                consultation.createdAt,
                                consultationAnswer.createdAt.as("answeredAt"),
                                answerer.name.as("answerer")))
                        .from(consultation)
                        .leftJoin(consultationAnswer).on(consultationAnswer.consultation.eq(consultation))
                        .leftJoin(answerer).on(consultationAnswer.member.eq(answerer))
                        .where(consulSearchFilter(filter, keyword),
                                consulDateFilter(toDate, fromDate))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(consultation.id.desc())
                        .fetch();

        Long count =
                queryFactory.select(consultation.count())
                        .from(consultation)
                        .leftJoin(consultationAnswer).on(consultationAnswer.consultation.eq(consultation))
                        .leftJoin(answerer).on(consultationAnswer.member.eq(answerer))
                        .where(consulSearchFilter(filter, keyword),
                                consulDateFilter(toDate, fromDate))
                        .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    // QnA 조건 추가
    private BooleanExpression consulSearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter) && StringUtils.hasText(keyword)) {
            return switch (filter) {
                case "title" -> consultation.subject.contains(keyword);
                case "writer" -> consultation.member.name.contains(keyword);
                case "classification" -> consultation.classification.eq(ConsulClassification.valueOf(keyword));
                default -> consultation.orders.orderNumber.contains(keyword);
            };
        } else {
            return null;
        }
    }

    // 날짜 조건 추가
    private BooleanExpression consulDateFilter(String toDate, String fromDate) {
        if (StringUtils.hasText(toDate) && StringUtils.hasText(fromDate)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate parseToDate = LocalDate.parse(toDate, dateFormatter).plusDays(1);
                LocalDate parseFromDate = LocalDate.parse(fromDate, dateFormatter);

                return consultation.createdAt.between(parseFromDate.atStartOfDay(), parseToDate.atStartOfDay());
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

    //커뮤니티 글 전체 리스트
    public Page<ArticleListDto> findByArticleAndFilterAndKeyword(Pageable pageable, String filter, String keyword) {
        QArticle article = QArticle.article;
        QComment comment = QComment.comment;
        QLikeArticle likeArticle = QLikeArticle.likeArticle;

        List<ArticleListDto> articleList =
                queryFactory.select(Projections.fields(ArticleListDto.class,
                                article.id.as("id"),
                                article.category.id.as("categoryId"),
                                article.title,
                                article.member,
                                article.attach,
                                article.viewCnt,
                                Expressions.as(
                                        JPAExpressions
                                                .select(likeArticle.count())
                                                .from(likeArticle)
                                                .where(likeArticle.article.eq(article)),
                                        "likeCnt"
                                ),
                                Expressions.as(
                                        JPAExpressions
                                                .select(comment.count())
                                                .from(comment)
                                                .where(comment.article.eq(article)),
                                        "commentCnt"
                                ),
                                article.createdAt
                        ))
                        .from(article)
                        .join(member).on(article.member.eq(member))
                        .where(articleSearchFilter(filter, keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(article.id.desc())
                        .fetch();
        // 카운트
        Long count = queryFactory.select(article.count())
                .from(article)
                .join(member).on(article.member.eq(member))
                .where(articleSearchFilter(filter, keyword))
                .fetchOne();

        return new PageImpl<>(articleList, pageable, count);
    }

    //커뮤니티 카테고리별 글 리스트
    public Page<ArticleListDto> findByArticleAndCategoryAndFilterAndKeyword(Pageable pageable, Long category, String filter, String keyword) {
        QArticle article = QArticle.article;
        QComment comment = QComment.comment;
        QLikeArticle likeArticle = QLikeArticle.likeArticle;

        List<ArticleListDto> articleList =
                queryFactory.select(Projections.fields(ArticleListDto.class,
                                article.id.as("id"),
                                article.category.id.as("categoryId"),
                                article.title,
                                article.member,
                                article.attach,
                                article.viewCnt,
                                Expressions.as(
                                        JPAExpressions
                                                .select(likeArticle.count())
                                                .from(likeArticle)
                                                .where(likeArticle.article.eq(article)),
                                        "likeCnt"
                                ),
                                Expressions.as(
                                        JPAExpressions
                                                .select(comment.count())
                                                .from(comment)
                                                .where(comment.article.eq(article)),
                                        "commentCnt"
                                ),
                                article.createdAt
                        ))
                        .from(article)
                        .join(member).on(article.member.eq(member))
                        .where(article.category.id.eq(category), articleSearchFilter(filter, keyword))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(article.id.desc())
                        .fetch();
        // 카운트
        Long count = queryFactory.select(article.count())
                .from(article)
                .join(member).on(article.member.eq(member))
                .where(article.category.id.eq(category), articleSearchFilter(filter, keyword))
                .fetchOne();

        return new PageImpl<>(articleList, pageable, count);
    }

    //상태 조건 추가
    private BooleanExpression articleSearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter) && StringUtils.hasText(keyword)) {
            return switch (filter) {
                case "제목+내용" -> article.title.contains(keyword).or(article.content.contains(keyword));
                case "제목" -> article.title.contains(keyword);
                case "내용" -> article.content.contains(keyword);
                case "작성자" -> article.member.nickname.contains(keyword);
                default -> null;
            };
        } else {
            return null;
        }
    }


    // 신고 목록
    public Page<ReportListDto> findByReportList(Pageable pageable, String filter, String keyword, String
            toDate, String fromDate) {
        QReview review = new QReview("review");
        QComment comment = new QComment("comment");
        QReport subReport = new QReport("subReport");

        List<ReportListDto> list =
                queryFactory.select(Projections.fields(ReportListDto.class,
                                report.id,
                                report.content,
                                report.member.name.as("reporter"),
                                report.reportedAt,
                                report.processedAt,
                                ExpressionUtils.as(
                                        JPAExpressions.select(
                                                        new CaseBuilder()
                                                                .when(report.article.isNotNull()).then("커뮤니티")
                                                                .when(report.comment.isNotNull()).then("댓글")
                                                                .otherwise("리뷰")
                                                )
                                                .from(subReport)
                                                .where(subReport.id.eq(report.id)),
                                        "classification")
                        ))
                        .from(report)
                        .leftJoin(article).on(report.article.eq(article))
                        .leftJoin(review).on(report.review.eq(review))
                        .leftJoin(comment).on(report.comment.eq(comment))
                        .where(reportSearchFilter(filter, keyword),
                                reportDateFilter(toDate, fromDate))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(report.id.desc())
                        .fetch();
        Long count =
                queryFactory.select(report.count())
                        .from(report)
                        .leftJoin(article).on(report.article.eq(article))
                        .leftJoin(review).on(report.review.eq(review))
                        .leftJoin(comment).on(report.comment.eq(comment))
                        .where(reportSearchFilter(filter, keyword),
                                reportDateFilter(toDate, fromDate))
                        .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    // QnA 조건 추가
    private BooleanExpression reportSearchFilter(String filter, String keyword) {
        if (StringUtils.hasText(filter) && StringUtils.hasText(keyword)) {
            return switch (filter) {
                case "content" -> report.content.contains(keyword);
                default -> report.member.name.contains(keyword);
            };
        } else {
            return null;
        }
    }


    // 날짜 조건 추가
    private BooleanExpression reportDateFilter(String toDate, String fromDate) {
        if (StringUtils.hasText(toDate) && StringUtils.hasText(fromDate)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate parseToDate = LocalDate.parse(toDate, dateFormatter).plusDays(1);
                LocalDate parseFromDate = LocalDate.parse(fromDate, dateFormatter);

                return report.reportedAt.between(parseFromDate.atStartOfDay(), parseToDate.atStartOfDay());
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
}