package com.shop.core.auth.application;

public interface UserDetailsService {

    boolean verifyUser(String email, String password);
}
