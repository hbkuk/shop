package com.shop.core.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;

    public static AuthResponse of(String accessToken) {
        return new AuthResponse(accessToken);
    }
}
