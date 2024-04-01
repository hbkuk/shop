package com.shop.core.admin.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminTokenResponse {

    private String accessToken;

    public static AdminTokenResponse of(String token) {
        return new AdminTokenResponse(token);
    }
}
