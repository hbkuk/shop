package com.shop.core.adminAuth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminGithubAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

}
