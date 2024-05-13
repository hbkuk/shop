package com.shop.core.adminAuth.fixture;

import java.util.Arrays;

public enum AdminGithubFixture {

    황병국("github_admin_code_001", "github_admin_token_001", "hwang@email.com", "010-1234-5678");

    public final String code;

    public final String token;

    public final String email;

    public final String phoneNumber;

    AdminGithubFixture(String code, String token, String email, String phoneNumber) {
        this.code = code;
        this.token = token;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static String findTokenByCode(String code) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.getCode().equals(code))
                .findFirst()
                .map(AdminGithubFixture::getToken)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코드입니다."));
    }

    public static String findEmailByToken(String token) {
        return Arrays.stream(values())
                .filter(tokenFixture -> tokenFixture.getToken().equals(token))
                .findFirst()
                .map(AdminGithubFixture::getEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토큰입니다."));
    }

    public String getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}
