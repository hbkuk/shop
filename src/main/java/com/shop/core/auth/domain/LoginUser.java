package com.shop.core.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUser implements UserDetail {
    private final String email;

    public static LoginUser of(String email) {
        return new LoginUser(email);
    }


    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
