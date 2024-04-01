package com.shop.core.admin.auth.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundAdminException extends BadRequestException {
    public NotFoundAdminException(ErrorType errorType) {
        super(errorType);
    }
}
