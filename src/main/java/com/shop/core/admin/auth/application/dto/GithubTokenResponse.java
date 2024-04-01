package com.shop.core.admin.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GithubTokenResponse {

    private String accessToken;
    private String scope;
    private String tokenType;

    public static GithubTokenResponse of(String token, String scope, String tokenType) {
        return new GithubTokenResponse(token, scope, tokenType);
    }
}
