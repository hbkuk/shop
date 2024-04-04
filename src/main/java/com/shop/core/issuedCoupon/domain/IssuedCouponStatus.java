package com.shop.core.issuedCoupon.domain;

import lombok.Getter;

@Getter
public enum IssuedCouponStatus {
    ACTIVE,
    USED,
    EXPIRED,
    CANCELED
}
