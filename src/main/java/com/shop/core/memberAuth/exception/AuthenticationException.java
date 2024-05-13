package com.shop.core.memberAuth.exception;

import com.shop.common.exception.ErrorType;
import com.shop.common.exception.ForbiddenException;

public class AuthenticationException extends ForbiddenException {

    public AuthenticationException(ErrorType errorType) {
        super(errorType);
    }
}
