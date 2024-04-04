package com.shop.core.coupon.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class CouponExhaustedException extends BadRequestException {
    public CouponExhaustedException(ErrorType errorType) {
        super(errorType);
    }
}
