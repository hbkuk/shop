package com.shop.core.point.application.dto;

import com.shop.core.point.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoResponse {

    private String paymentId;

    private String email;

    private PaymentStatus paymentStatus;

    private int amount;

    // TODO: 추가 필드 확인

    public static PaymentInfoResponse of(String paymentId, String email, PaymentStatus paymentStatus, int amount) {
        return new PaymentInfoResponse(paymentId, email, paymentStatus, amount);
    }
}
