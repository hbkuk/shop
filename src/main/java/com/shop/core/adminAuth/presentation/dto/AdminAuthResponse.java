package com.shop.core.adminAuth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public static AdminAuthResponse of(String accessToken) {
        return new AdminAuthResponse(accessToken);
    }
}
