package com.shop.core.product.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundProductException extends BadRequestException {

    public NotFoundProductException(ErrorType errorType) {
        super(errorType);
    }
}
