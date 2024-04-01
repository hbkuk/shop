package com.shop.core.admin.auth.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;
import com.shop.common.exception.UnauthorizedException;

public class NonMatchingSignupChannelException extends BadRequestException {

    public NonMatchingSignupChannelException(ErrorType errorType) {
        super(errorType);
    }
}
