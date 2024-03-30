package com.shop.core.address.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundAddressException extends BadRequestException {
    public NotFoundAddressException(ErrorType errorType) {
        super(errorType);

    }
}
