package com.shop.core.store.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundStoreException extends BadRequestException {
    public NotFoundStoreException(ErrorType errorType) {
        super(errorType);
    }
}
