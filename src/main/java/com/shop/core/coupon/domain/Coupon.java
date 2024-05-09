package com.shop.core.coupon.domain;

import com.shop.common.entity.BaseEntity;
import com.shop.common.exception.ErrorType;
import com.shop.core.coupon.exception.CouponExhaustedException;
import com.shop.core.coupon.exception.CouponIssuanceNotAllowedException;
import com.shop.core.coupon.exception.CouponStatusChangeNotAllowedException;
import com.shop.core.coupon.exception.InsufficientCouponQuantityException;
import com.shop.core.issuedCoupon.domain.IssuedCoupon;
import com.shop.core.issuedCoupon.domain.IssuedCoupons;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private int maxDiscountAmount;

    private int discountAmount;

    private int remainingIssueCount;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @JoinColumn(name = "ADMIN_EMAIL")
    private String issuerAdminEmail;

    @Embedded
    private IssuedCoupons issuedCoupons = new IssuedCoupons();

    @Version
    private Integer version;

    public Coupon(String name, String description, int maxDiscountAmount, int discountAmount, int remainingIssueCount, CouponStatus couponStatus, String issuerAdminEmail) {
        this.name = name;
        this.description = description;
        this.maxDiscountAmount = maxDiscountAmount;
        this.discountAmount = discountAmount;
        this.remainingIssueCount = remainingIssueCount;
        this.couponStatus = couponStatus;
        this.issuerAdminEmail = issuerAdminEmail;
    }

    @PreUpdate
    public void updateStatusIfExhausted() {
        if (remainingIssueCount == 0) {
            this.couponStatus = CouponStatus.EXHAUSTED;
        }
    }


    public void issue(List<IssuedCoupon> issuedCoupons) {
        verifyIssuable();
        verifyCountSufficient(issuedCoupons);

        this.issuedCoupons.addAll(issuedCoupons);
        this.remainingIssueCount -= issuedCoupons.size();
    }

    private void verifyIssuable() {
        if (this.couponStatus == CouponStatus.EXHAUSTED) {
            throw new CouponExhaustedException(ErrorType.COUPON_EXHAUSTED);
        }
        if (this.couponStatus != CouponStatus.ISSUABLE) {
            throw new CouponIssuanceNotAllowedException(ErrorType.COUPON_ISSUANCE_NOT_ALLOWED);
        }
    }

    private void verifyCountSufficient(List<IssuedCoupon> issuedCoupons) {
        if (remainingIssueCount < issuedCoupons.size()) {
            throw new InsufficientCouponQuantityException(ErrorType.COUPON_INSUFFICIENT);
        }
    }

    public Coupon updateStatus(CouponStatus status) {
        if (!this.couponStatus.isStatusChangeAllowed(status)) {
            throw new CouponStatusChangeNotAllowedException(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED);
        }

        this.couponStatus = status;
        return this;
    }

    public List<IssuedCoupon> getIssuedCoupons() {
        return this.issuedCoupons.getIssuedCoupons();
    }
}
