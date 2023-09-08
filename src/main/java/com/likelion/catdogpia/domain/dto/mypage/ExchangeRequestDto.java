package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefund;
import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefundReason;
import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefundType;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExchangeRequestDto {

    // 주문 상품 id (OrderProduct)
    private Long opId;

    // 사유 (ExchangeRefund)
    private ExchangeRefundReason reason;

    // 상세 사유
    private String detailReason;

    // 교환 희망 옵션 - 색상 (ExchangeRefund)
    private String size;

    // 교환 희망 옵션 - 사이즈 (ExchangeRefund)
    private String color;

    public ExchangeRefund toEntity(OrderProduct orderProduct) {
        return ExchangeRefund.builder()
                .exchangeOrRefund(ExchangeRefundType.EXCHANGE)
                .reason(reason)
                .detailReason(detailReason)
                .size(size)
                .color(color)
                .isApproval(null)
                .totalRefundAmount(null)
                .exchangeRequestedAt(LocalDateTime.now())
                .exchangeCompletedAt(null)
                .refundRequestedAt(null)
                .refundCompletedAt(null)
                .orderProduct(orderProduct)
                .build();
    }

}
