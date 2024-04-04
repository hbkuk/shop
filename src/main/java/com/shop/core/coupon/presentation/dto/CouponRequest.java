package com.shop.core.coupon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.coupon.domain.Coupon;
import com.shop.core.coupon.domain.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequest {

    public String name;

    public String description;

    @JsonProperty("max_discount_amount")
    public int maxDiscountAmount;

    @JsonProperty("discount_amount")
    public int discountAmount;

    @JsonProperty("remaining_issue_count")
    public int remainingIssueCount;

    public static CouponRequest of(String name, String description, int maxDiscountAmount, int discountAmount, int remainingIssueCount) {
        return new CouponRequest(name, description, maxDiscountAmount, discountAmount, remainingIssueCount);
    }

    public Coupon toEntity(LocalDateTime createdAt, CouponStatus couponStatus, Long adminId) {
        return new Coupon(name, description, maxDiscountAmount, discountAmount, remainingIssueCount, createdAt, couponStatus, adminId);
    }
}
