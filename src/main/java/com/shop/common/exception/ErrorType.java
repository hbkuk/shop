package com.shop.common.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    REQUEST_EXCEPTION(9001, "http 요청 에러입니다."),
    INVALID_PATH(9002, "잘못된 경로입니다."),
    UNPROCESSABLE_ENTITY(9003, "요청 데이터가 유효하지 않습니다."),
    UNHANDLED_EXCEPTION(9999, "예상치 못한 예외입니다.");

    private final int code;
    private final String message;

    ErrorType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
