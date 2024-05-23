package com.shop.core.point.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.point.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWebhookRequest {

    @JsonProperty("payment_id")
    private String paymentId;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

    public static PaymentWebhookRequest of(String number, PaymentStatus paymentStatus) {
        return new PaymentWebhookRequest(number, paymentStatus);
    }
}
