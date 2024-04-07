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

    private List<Long> memberIds;

    public CouponIssueRequest(List<Long> memberIds) {
        this.memberIds = memberIds;
    }


    public static CouponIssueRequest mergeCouponId(Long couponId, CouponIssueRequest request) {
        return new CouponIssueRequest(couponId, request.getMemberIds());
    }

    public static CouponIssueRequest of(List<Long> memberIds) {
        return new CouponIssueRequest(memberIds);
    }

    public static CouponIssueRequest of(Long couponId, List<Long> memberIds) {
        return new CouponIssueRequest(couponId, memberIds);
    }

    public List<IssuedCoupon> toEntity(LocalDateTime issuedAt, LocalDateTime expiredAt, IssuedCouponStatus status, Coupon coupon) {
        return memberIds.stream()
                .map(memberId -> new IssuedCoupon(memberId, issuedAt, expiredAt, status, coupon))
                .collect(Collectors.toList());
    }
}
