package com.shop.core.coupon.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class CouponIssuanceNotAllowedException extends BadRequestException {
    public CouponIssuanceNotAllowedException(ErrorType errorType) {
        super(errorType);
    }
}
