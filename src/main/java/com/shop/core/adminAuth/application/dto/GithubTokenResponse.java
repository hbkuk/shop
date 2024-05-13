package com.shop.core.adminAuth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GithubTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;

    public static GithubTokenResponse of(String token, String scope, String tokenType) {
        return new GithubTokenResponse(token, scope, tokenType);
    }
}
