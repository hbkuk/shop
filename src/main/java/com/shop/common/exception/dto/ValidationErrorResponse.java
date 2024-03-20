package com.shop.common.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ValidationErrorResponse {

    private int errorCode;
    private Map<String, String> message;

    public ValidationErrorResponse(final int errorCode, final Map<String, String> message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
