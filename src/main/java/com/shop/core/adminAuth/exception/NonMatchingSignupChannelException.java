package com.shop.core.adminAuth.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NonMatchingSignupChannelException extends BadRequestException {

    public NonMatchingSignupChannelException(ErrorType errorType) {
        super(errorType);
    }
}
