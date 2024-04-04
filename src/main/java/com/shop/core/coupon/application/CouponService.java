package com.shop.core.coupon.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.admin.auth.application.AdminAuthService;
import com.shop.core.admin.auth.domain.Admin;
import com.shop.core.auth.domain.LoginUser;
import com.shop.core.coupon.domain.Coupon;
import com.shop.core.coupon.domain.CouponRepository;
import com.shop.core.coupon.domain.CouponStatus;
import com.shop.core.coupon.exception.NotFoundCouponException;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.coupon.presentation.dto.CouponResponse;
import com.shop.core.coupon.presentation.dto.CouponStatusRequest;
import com.shop.core.issuedCoupon.domain.IssuedCoupon;
import com.shop.core.issuedCoupon.domain.IssuedCouponStatus;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueResponse;
import com.shop.core.member.application.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final AdminAuthService adminAuthService;

    private final MemberService memberService;

    private final CouponRepository couponRepository;

    @Transactional
    public CouponResponse createCoupon(CouponRequest request, LoginUser loginUser) {
        Admin admin = adminAuthService.findAdminByEmail(loginUser.getEmail());
        Coupon coupon = request.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, admin.getId());

        return CouponResponse.of(couponRepository.save(coupon));
    }

    public CouponResponse findById(Long couponId, LoginUser loginUser) {
        Admin admin = adminAuthService.findAdminByEmail(loginUser.getEmail());
        Coupon coupon = findCouponById(couponId);

        return CouponResponse.of(coupon);
    }

    public Coupon findCouponById(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(() -> new NotFoundCouponException(ErrorType.NOT_FOUND_COUPON));
    }

    @Transactional
    public CouponIssueResponse issueCoupon(CouponIssueRequest request, LoginUser loginAdmin) {
        Admin admin = adminAuthService.findAdminByEmail(loginAdmin.getEmail());

        request.getMemberIds().forEach(memberService::findMemberById);
        Coupon coupon = findCouponById(request.getCouponId());

        List<IssuedCoupon> issuedCoupons = request.toEntity(LocalDateTime.now(), LocalDateTime.now().plusMonths(1), IssuedCouponStatus.ACTIVE, coupon);
        coupon.issueCoupon(issuedCoupons);

        return CouponIssueResponse.of(issuedCoupons);
    }

    public CouponIssueResponse findIssueCoupons(Long couponId, LoginUser loginAdmin) {
        Admin admin = adminAuthService.findAdminByEmail(loginAdmin.getEmail());
        Coupon coupon = findCouponById(couponId);

        List<IssuedCoupon> issuedCoupons = coupon.getIssuedCoupons();
        return CouponIssueResponse.of(issuedCoupons);
    }

    @Transactional
    public void updateStatus(CouponStatusRequest couponStatusRequest, LoginUser loginAdmin) {
        Admin admin = adminAuthService.findAdminByEmail(loginAdmin.getEmail());
        Coupon coupon = findCouponById(couponStatusRequest.getCouponId());

        coupon.updateStatus(couponStatusRequest.getStatus());
    }
}
