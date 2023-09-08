package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefund;
import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefundReason;
import com.likelion.catdogpia.domain.entity.mypage.ExchangeRefundType;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefundRequestDto {

    // 주문 상품 id (OrderProduct)
    private Long opId;

    // 사유 (ExchangeRefund)
    private ExchangeRefundReason reason;

    // 상세 사유
    private String detailReason;

    public ExchangeRefund toEntity(OrderProduct orderProduct) {
        return ExchangeRefund.builder()
                .exchangeOrRefund(ExchangeRefundType.REFUND)
                .reason(reason)
                .detailReason(detailReason)
                .isApproval(null)
                .totalRefundAmount(null)
                .exchangeRequestedAt(null)
                .exchangeCompletedAt(null)
                .refundRequestedAt(LocalDateTime.now())
                .refundCompletedAt(null)
                .orderProduct(orderProduct)
                .build();
    }

}
