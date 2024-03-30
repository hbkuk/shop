package com.shop.core.member.exception;

import com.shop.common.exception.ErrorType;
import com.shop.common.exception.UnauthorizedException;

public class NonMatchingMemberException extends UnauthorizedException {
    public NonMatchingMemberException(ErrorType errorType) {
        super(errorType);
    }
}
