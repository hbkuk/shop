package com.shop.core.issuedCoupon.domain;

import com.shop.common.domain.base.BaseEntity;
import com.shop.core.coupon.domain.Coupon;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedCoupon extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "COUPON_CODE", columnDefinition = "BINARY(16)")
    private UUID code;

    @JoinColumn(name = "MEMBER_EMAIL")
    private String memberEmail;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private IssuedCouponStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coupon coupon;

    public IssuedCoupon(String memberEmail, LocalDateTime issuedAt, LocalDateTime expiredAt, IssuedCouponStatus status, Coupon coupon) {
        this.memberEmail = memberEmail;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.status = status;
        this.coupon = coupon;
    }

    public boolean isSameOwner(IssuedCoupon issuedCoupon) {
        return memberEmail.equals(issuedCoupon.memberEmail);
    }
}
