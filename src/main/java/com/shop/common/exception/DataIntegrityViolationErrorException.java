package com.shop.common.exception;

public class DataIntegrityViolationErrorException extends InternalServerErrorException {
    private ErrorType errorType;

    public DataIntegrityViolationErrorException(ErrorType errorType) {
        super(errorType);
    }
}
