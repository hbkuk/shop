package com.shop.core.point.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.WebClientUtil;
import com.shop.core.point.application.dto.PaymentInfoResponse;
import com.shop.core.point.application.dto.WebhookTokenWrapperResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebhookClient {

    @Value("${payment-webhook.client.api-key}") // TODO: 인증을 담당하는 Client Component 분리 고려
    private String apiKey;

    @Value("${payment-webhook.client.api-secret-key}")
    private String apiSecretKey;

    @Value("${payment-webhook.base-url}")
    private String baseUrl;

    @Value("${payment-webhook.get-access-token-url}")
    private String getAccessTokenUrl;

    @Value("${payment-webhook.get-payment-info-url}")
    private String getPaymentInfoUrl;

    private final WebClientUtil webClientUtil;

    public WebhookClient(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
    }

    public PaymentInfoResponse requestPaymentInfo(String paymentId) {
        Map<String, String> body = new HashMap<>();
        body.put("key", apiKey);
        body.put("secrey_key", apiSecretKey);

        String accessToken = webClientUtil.post(baseUrl + getAccessTokenUrl, body, MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE), WebhookTokenWrapperResponse.class)
                .block()
                .getWebhookTokenResponse()
                .getAccessToken();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);

        return webClientUtil.get(baseUrl + String.format(getPaymentInfoUrl, paymentId), headers, PaymentInfoResponse.class)
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        return new IllegalArgumentException(ErrorType.INVALID_PAYMENT_ID.getMessage());
                    }
                    return new RuntimeException(ErrorType.UNHANDLED_EXCEPTION.getMessage());
                })
                .block();
    }

}
