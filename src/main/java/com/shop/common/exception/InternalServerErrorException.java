package com.shop.common.exception;

import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException {

    private final int code;

    public InternalServerErrorException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }
}
