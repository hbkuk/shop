package com.shop.core.admin.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminGithubProfileResponse {

    private String email;

    public static AdminGithubProfileResponse of(String email) {
        return new AdminGithubProfileResponse(email);
    }
}
