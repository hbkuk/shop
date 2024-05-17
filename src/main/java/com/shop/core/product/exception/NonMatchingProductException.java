package com.shop.core.product.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NonMatchingProductException extends BadRequestException {
    public NonMatchingProductException(ErrorType errorType) {
        super(errorType);
    }
}
