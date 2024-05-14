package com.shop.core.store.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class DuplicateStoreNameException extends BadRequestException {

    public DuplicateStoreNameException(ErrorType errorType) {
        super(errorType);
    }
}
