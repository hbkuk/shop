package com.shop.core.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String password;

    public static AuthRequest of(String email, String password) {
        return new AuthRequest(email, password);
    }
}
