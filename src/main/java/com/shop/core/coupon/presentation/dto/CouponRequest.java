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

    private String name;

    private String description;

    @JsonProperty("max_discount_amount")
    private int maxDiscountAmount;

    @JsonProperty("discount_amount")
    private int discountAmount;

    @JsonProperty("remaining_issue_count")
    private int remainingIssueCount;

    public static CouponRequest of(String name, String description, int maxDiscountAmount, int discountAmount, int remainingIssueCount) {
        return new CouponRequest(name, description, maxDiscountAmount, discountAmount, remainingIssueCount);
    }

    public Coupon toEntity(LocalDateTime createdAt, CouponStatus couponStatus, String adminEmail) {
        return new Coupon(name, description, maxDiscountAmount, discountAmount, remainingIssueCount, createdAt, couponStatus, adminEmail);
    }
}
