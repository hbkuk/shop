package com.shop.core.userSecurity.util;

import java.security.SecureRandom;

public class UserSecurityUtil {
    public static String createSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[20];
        sr.nextBytes(salt);

        StringBuffer buffer = new StringBuffer();
        for (byte b : salt) {
            buffer.append(String.format("%02x", b));
        }

        return buffer.toString();
    }
}
