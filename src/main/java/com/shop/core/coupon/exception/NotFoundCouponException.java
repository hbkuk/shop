package com.shop.core.coupon.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class NotFoundCouponException extends BadRequestException {
    public NotFoundCouponException(ErrorType errorType) {
        super(errorType);
    }
}
