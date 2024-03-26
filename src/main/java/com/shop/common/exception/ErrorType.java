package com.shop.common.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    INVALID_TOKEN(1000, "유효하지 않은 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(1001, "유효기간이 만료된 액세스 토큰입니다."),

    NOT_FOUND_MEMBER(3001, "존재하지 않는 회원입니다."),
    DUPLICATE_MEMBER_EMAIL(3002, "중복된 회원 이메일입니다."),
    PASSWORD_MISMATCH(3003, "비밀번호가 동일하지 않습니다."),

    REQUEST_EXCEPTION(9001, "http 요청 에러입니다."),
    INVALID_PATH(9002, "잘못된 경로입니다."),
    UNPROCESSABLE_ENTITY(9003, "요청 데이터가 유효하지 않습니다."),
    DATA_INTEGRITY_VIOLATION(9004, "예상하지 못한 데이터가 입력되었습니다."),
    UNHANDLED_EXCEPTION(9999, "예상치 못한 예외입니다.");

    private final int code;
    private final String message;

    ErrorType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
