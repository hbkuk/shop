package com.shop.core.coupon.application;

import com.shop.common.event.Event;
import com.shop.common.exception.ErrorType;
import com.shop.common.reTry.IsTryAgain;
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
import com.shop.core.notification.domain.NotificationEvent;
import com.shop.core.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final AdminAuthService adminAuthService;

    private final MemberService memberService;

    private final CouponRepository couponRepository;

    public CouponResponse findById(Long couponId, LoginUser loginUser) {
        verifyAdminByEmail(loginUser);

        Coupon coupon = findCouponById(couponId);
        return CouponResponse.of(coupon);
    }

    public CouponIssueResponse findIssueCoupons(Long couponId, LoginUser loginAdmin) {
        verifyAdminByEmail(loginAdmin);

        Coupon coupon = findCouponById(couponId);
        return CouponIssueResponse.of(coupon.getIssuedCoupons());
    }

    public Coupon findCouponById(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(() -> new NotFoundCouponException(ErrorType.NOT_FOUND_COUPON));
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request, LoginUser loginUser) {
        Admin admin = verifyAdminByEmail(loginUser);

        Coupon coupon = createCoupon(request, admin.getEmail());
        return CouponResponse.of(couponRepository.save(coupon));
    }

    @Transactional
    public void updateStatus(CouponStatusRequest request, LoginUser loginAdmin) {
        verifyAdminByEmail(loginAdmin);

        Coupon coupon = findCouponById(request.getCouponId());
        coupon.updateStatus(request.getStatus());
    }

    @IsTryAgain
    @Transactional
    public CouponIssueResponse issueCoupon(CouponIssueRequest request, LoginUser loginAdmin) {
        log.info(String.format("publish thread id: %s", Thread.currentThread().getId()));
        verifyAdminByEmail(loginAdmin);
        verifyMembersByEmail(request.getMemberEmails());

        Coupon coupon = couponRepository.findByCouponId(request.getCouponId());
        List<IssuedCoupon> issuedCoupons = toIssuedCoupons(request, coupon);
        coupon.issue(issuedCoupons);

        Event.publish(NotificationEvent.of(request.getMemberEmails(), loginAdmin.getEmail(), NotificationType.COUPON_ISSUANCE));
        return CouponIssueResponse.of(issuedCoupons);
    }

    private List<IssuedCoupon> toIssuedCoupons(CouponIssueRequest request, Coupon coupon) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiredAt = issuedAt.plusMonths(1);

        return request.toEntity(issuedAt, expiredAt, IssuedCouponStatus.ACTIVE, coupon);
    }

    private void verifyMembersByEmail(List<String> memberEmails) {
        memberEmails.forEach(memberService::findMemberByEmail);
    }

    private Admin verifyAdminByEmail(LoginUser loginUser) {
        return adminAuthService.findAdminByEmail(loginUser.getEmail());
    }

    private Coupon createCoupon(CouponRequest request, String adminEmail) {
        LocalDateTime createdAt = LocalDateTime.now();
        return request.toEntity(createdAt, CouponStatus.ISSUABLE, adminEmail);
    }
}
