package com.shop.common.exception;

public class NotFoundDataException extends BadRequestException {
    public NotFoundDataException(ErrorType errorType) {
        super(errorType);
    }
}
