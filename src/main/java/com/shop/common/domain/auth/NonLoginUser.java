package com.shop.common.domain.auth;

public class NonLoginUser implements UserDetail {
    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
