package com.shop.core.coupon.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select coupon from Coupon coupon where coupon.id = :couponId")
    Coupon findByCouponIdLock(@Param("couponId") Long couponId);

    @Query("select c from Coupon c join fetch c.issuedCoupons where c.id = :couponId")
    Coupon findCouponWithIssuedCoupons(Long couponId);
}
