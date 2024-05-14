package com.shop.common.auth;

import com.shop.common.exception.ErrorType;
import com.shop.common.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException(ErrorType errorType) {
        super(errorType);
    }
}
