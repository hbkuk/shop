package com.shop.core.coupon.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class CouponAlreadyIssuedException extends BadRequestException {
    public CouponAlreadyIssuedException(ErrorType errorType) {
        super(errorType);
    }
}
