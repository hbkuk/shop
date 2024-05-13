package com.shop.core.memberAuth.domain;

public class NonLoginUser implements UserDetail {
    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
