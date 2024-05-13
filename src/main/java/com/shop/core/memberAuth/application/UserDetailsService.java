package com.shop.core.memberAuth.application;

public interface UserDetailsService {

    boolean verifyUser(String email, String password);
}
