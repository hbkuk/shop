package com.shop.core.issuedCoupon.domain;

import com.shop.core.coupon.domain.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "MEMBER_ID")
    private Long memberId;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private IssuedCouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    public IssuedCoupon(Long memberId, LocalDateTime issuedAt, LocalDateTime expiredAt, IssuedCouponStatus status, Coupon coupon) {
        this.memberId = memberId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.status = status;
        this.coupon = coupon;
    }
}
