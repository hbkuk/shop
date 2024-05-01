package com.shop.core.coupon.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CouponStatus {
    ISSUABLE,
    STOPPED_ISSUANCE,
    DELETED,
    EXHAUSTED;

    @JsonCreator
    public static CouponStatus from(String value) {
        return Arrays.stream(values())
                .filter(couponStatus -> couponStatus.name().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isStatusChangeAllowed(CouponStatus statusToChange) {
        if (this == ISSUABLE) {
            return statusToChange == STOPPED_ISSUANCE || statusToChange == CouponStatus.DELETED;
        }
        if (this == STOPPED_ISSUANCE) {
            return statusToChange == ISSUABLE || statusToChange == CouponStatus.DELETED;
        }
        return false;
    }
}
