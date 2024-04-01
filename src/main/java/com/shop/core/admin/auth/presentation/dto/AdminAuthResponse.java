package com.shop.core.admin.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthResponse {

    private String accessToken;

    public static AdminAuthResponse of(String accessToken) {
        return new AdminAuthResponse(accessToken);
    }
}
