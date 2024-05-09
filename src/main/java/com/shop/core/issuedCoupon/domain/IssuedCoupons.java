package com.shop.core.issuedCoupon.domain;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
public class IssuedCoupons {

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<IssuedCoupon> issuedCoupons = new ArrayList<>();

    public void addAll(List<IssuedCoupon> couponsToIssue) {
        this.issuedCoupons.addAll(couponsToIssue);
    }

    public boolean isAnyCouponAlreadyIssued(List<IssuedCoupon> couponsToIssue) {
        return couponsToIssue.stream()
                .anyMatch(couponToIssue -> issuedCoupons.stream()
                        .anyMatch(couponToIssue::isSameOwner));
    }
}
