package com.shop.core.auth.domain;

public class NonLoginUser implements UserDetail {
    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
