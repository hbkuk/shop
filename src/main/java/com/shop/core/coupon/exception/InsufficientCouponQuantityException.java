package com.shop.core.coupon.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class InsufficientCouponQuantityException extends BadRequestException {
    public InsufficientCouponQuantityException(ErrorType errorType) {
        super(errorType);
    }
}
