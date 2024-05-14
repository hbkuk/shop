package com.shop.core.store.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NonMatchingStoreManagerException extends BadRequestException {
    public NonMatchingStoreManagerException(ErrorType errorType) {
        super(errorType);
    }
}
