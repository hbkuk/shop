package com.shop.core.point.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookTokenWrapperResponse {

    private String code;

    private String message;

    @JsonProperty("response")
    private WebhookTokenResponse webhookTokenResponse;

    public static WebhookTokenWrapperResponse of(String code, String message, WebhookTokenResponse webhookTokenResponse) {
        return new WebhookTokenWrapperResponse(code, message, webhookTokenResponse);
    }
}
