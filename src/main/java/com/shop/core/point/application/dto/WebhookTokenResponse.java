package com.shop.core.point.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebhookTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private String now;

    @JsonProperty("expired_at")
    private String expiredAt;

    public static WebhookTokenResponse of(String accessToken, String now, String expiredAt) {
        return new WebhookTokenResponse(accessToken, now, expiredAt);
    }
}
