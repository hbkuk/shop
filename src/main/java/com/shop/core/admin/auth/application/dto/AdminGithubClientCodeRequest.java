package com.shop.core.admin.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminGithubClientCodeRequest {

    private String code;

    private String clientId;

    private String clientSecret;

}
