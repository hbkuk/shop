package com.shop.core.issuedCoupon.domain;

import com.shop.core.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    @Query("select issuedCoupon from IssuedCoupon issuedCoupon where issuedCoupon.coupon = :coupon")
    List<IssuedCoupon> findByCoupon(@Param("coupon") Coupon coupon);
}
