package com.shop.core.notification.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class CannotNotificationReadException extends BadRequestException {
    public CannotNotificationReadException(ErrorType errorType) {
        super(errorType);
    }
}
