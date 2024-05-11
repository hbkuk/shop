package com.shop.core.storeManager.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class DuplicateEmailException extends BadRequestException {
    public DuplicateEmailException(ErrorType errorType) {
        super(errorType);
    }
}
