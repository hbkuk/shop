package com.shop.core.admin.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminGithubCodeRequest {

    private String code;
}
