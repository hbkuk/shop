package com.shop.core.coupon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.coupon.domain.CouponStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CouponStatusRequest {

    @JsonIgnore
    private Long couponId;

    @JsonProperty("coupon_status")
    private CouponStatus status;

    public CouponStatusRequest(CouponStatus status) {
        this.status = status;
    }

    public static CouponStatusRequest of(CouponStatus couponStatus) {
        return new CouponStatusRequest(couponStatus);
    }

    public static CouponStatusRequest of(Long couponId, CouponStatus couponStatus) {
        return new CouponStatusRequest(couponId, couponStatus);
    }

    public static CouponStatusRequest mergeCouponId(Long couponId, CouponStatusRequest request) {
        return new CouponStatusRequest(couponId, request.getStatus());
    }
}
