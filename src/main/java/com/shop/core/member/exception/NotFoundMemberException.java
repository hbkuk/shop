package com.shop.core.member.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundMemberException extends BadRequestException {
    public NotFoundMemberException(ErrorType errorType) {
        super(errorType);
    }
}
