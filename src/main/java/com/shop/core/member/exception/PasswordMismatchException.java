package com.shop.core.member.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class PasswordMismatchException extends BadRequestException {

    public PasswordMismatchException(ErrorType errorType) {
        super(errorType);
    }
}
