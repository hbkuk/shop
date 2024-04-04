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
public class CouponResponse {
    private Long id;

    private String name;

    private String description;

    @JsonProperty("max_discount_amount")
    private int maxDiscountAmount;

    @JsonProperty("discount_amount")
    private int discountAmount;

    @JsonProperty("remaining_issue_count")
    private int remainingIssueCount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("coupon_status")
    private CouponStatus couponStatus;

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getDescription(), coupon.getMaxDiscountAmount(), coupon.getDiscountAmount(), coupon.getRemainingIssueCount(), coupon.getCreatedAt(), coupon.getCouponStatus());
    }
}
