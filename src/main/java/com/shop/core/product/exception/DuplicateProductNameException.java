package com.shop.core.product.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class DuplicateProductNameException extends BadRequestException {
    public DuplicateProductNameException(ErrorType errorType) {
        super(errorType);
    }
}
