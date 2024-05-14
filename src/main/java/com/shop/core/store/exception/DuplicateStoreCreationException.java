package com.shop.core.store.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class DuplicateStoreCreationException extends BadRequestException {
    public DuplicateStoreCreationException(ErrorType errorType) {
        super(errorType);
    }
}
