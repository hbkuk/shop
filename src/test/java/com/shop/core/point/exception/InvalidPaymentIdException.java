package com.shop.core.point.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class InvalidPaymentIdException extends BadRequestException {

    public InvalidPaymentIdException(ErrorType errorType) {
        super(errorType);
    }
}
