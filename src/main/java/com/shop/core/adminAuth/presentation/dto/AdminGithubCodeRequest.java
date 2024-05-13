package com.shop.core.adminAuth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminGithubCodeRequest {

    private String code;

    public static AdminGithubCodeRequest of(String code) {
        return new AdminGithubCodeRequest(code);
    }
}
