package com.shop.core.issuedCoupon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.issuedCoupon.domain.IssuedCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssueResponse {

    @JsonProperty("issued_member_emails")
    private List<String> issuedMemberEmails;

    public static CouponIssueResponse of(List<IssuedCoupon> issuedCoupons) {
        return new CouponIssueResponse(issuedCoupons.stream().map(IssuedCoupon::getMemberEmail).collect(Collectors.toList()));
    }
}
