package com.shop.common.util;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;
import com.shop.core.point.application.dto.PaymentInfoResponse;
import com.shop.core.point.application.dto.WebhookTokenResponse;
import com.shop.core.point.application.dto.WebhookTokenWrapperResponse;
import com.shop.core.point.domain.PaymentStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.shop.core.member.fixture.MemberFixture.스미스;

@Profile("test")
@RestController
public class FakeWebhookController {

    @PostMapping("/users/getToken")
    public ResponseEntity<WebhookTokenWrapperResponse> getToken(Map<String, String> params) {
        WebhookTokenResponse webhookTokenResponse = WebhookTokenResponse.of("Bearer Valid Token", "now", "expired At");

        return ResponseEntity.ok().body(WebhookTokenWrapperResponse.of("200", "success", webhookTokenResponse));
    }

    @GetMapping("/payments/{paymentId}/balance")
    public ResponseEntity<PaymentInfoResponse> getPaymentInfo(@PathVariable String paymentId,
                                                              @RequestHeader("Authorization") String authorizationHeader) {
        if (paymentId.equals("9999999999")) {
            throw new BadRequestException(ErrorType.NOT_FOUND_DATA);
        }
        return ResponseEntity.ok().body(PaymentInfoResponse.of(paymentId, 스미스.이메일, PaymentStatus.PAID, 10000));
    }
}
