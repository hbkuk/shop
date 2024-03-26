package com.shop.core.memberSecurity.util;

import java.security.SecureRandom;

public class MemberSecurityUtil {
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
