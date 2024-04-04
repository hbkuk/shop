package com.shop.core.coupon.exception;

import com.shop.common.exception.BadRequestException;
import com.shop.common.exception.ErrorType;

public class CouponStatusChangeNotAllowedException extends BadRequestException {
    public CouponStatusChangeNotAllowedException(ErrorType errorType) {
        super(errorType);
    }
}
