package com.shop.core.memberAuth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public static AuthResponse of(String accessToken) {
        return new AuthResponse(accessToken);
    }
}
