package com.shop.common.reTry;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NoMoreTryException extends BadRequestException {
    public NoMoreTryException(ErrorType errorType) {
        super(errorType);
    }
}
