package com.shop.common.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PasswordSecurityManager {

    private final PasswordEncoder passwordEncoder;

    public String encryptedPassword(String password, String salt) {
        return passwordEncoder.encode(appendSalt(password, salt));
    }

    public boolean isPasswordMatchWithSalt(String rawPassword, String salt, String encryptedPassword) {
        String passwordWithSalt = appendSalt(rawPassword, salt);
        return passwordEncoder.matches(passwordWithSalt, encryptedPassword);
    }

    private static String appendSalt(String password, String salt) {
        return String.join("", salt, password);
    }
}
