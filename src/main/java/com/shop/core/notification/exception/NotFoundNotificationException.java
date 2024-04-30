package com.shop.core.notification.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundNotificationException extends BadRequestException {
    public NotFoundNotificationException(ErrorType errorType) {
        super(errorType);
    }
}
