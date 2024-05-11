package com.shop.core.storeManager.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundStoreManagerException extends BadRequestException {
    public NotFoundStoreManagerException(ErrorType errorType) {
        super(errorType);
    }
}
