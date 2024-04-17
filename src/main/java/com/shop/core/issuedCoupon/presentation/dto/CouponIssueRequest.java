package com.shop.core.issuedCoupon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.core.coupon.domain.Coupon;
import com.shop.core.issuedCoupon.domain.IssuedCoupon;
import com.shop.core.issuedCoupon.domain.IssuedCouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssueRequest {

    @JsonIgnore
    private Long couponId;

    private List<String> memberEmails;

    public CouponIssueRequest(List<String> memberEmails) {
        this.memberEmails = memberEmails;
    }

    public static CouponIssueRequest mergeCouponId(Long couponId, CouponIssueRequest request) {
        return new CouponIssueRequest(couponId, request.getMemberEmails());
    }

    public static CouponIssueRequest of(List<String> memberEmails) {
        return new CouponIssueRequest(memberEmails);
    }

    public static CouponIssueRequest of(Long couponId, List<String> memberEmails) {
        return new CouponIssueRequest(couponId, memberEmails);
    }

    public List<IssuedCoupon> toEntity(LocalDateTime issuedAt, LocalDateTime expiredAt, IssuedCouponStatus status, Coupon coupon) {
        return memberEmails.stream()
                .map(memberEmail -> new IssuedCoupon(memberEmail, issuedAt, expiredAt, status, coupon))
                .collect(Collectors.toList());
    }
}
