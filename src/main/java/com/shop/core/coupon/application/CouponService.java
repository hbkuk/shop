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

        Coupon coupon = createCoupon(request, admin.getId());
        return CouponResponse.of(couponRepository.save(coupon));
    }

    @Transactional
    public void updateStatus(CouponStatusRequest request, LoginUser loginAdmin) {
        verifyAdminByEmail(loginAdmin);

        Coupon coupon = findCouponById(request.getCouponId());
        coupon.updateStatus(request.getStatus());
    }

    @Transactional
    public CouponIssueResponse issueCoupon(CouponIssueRequest request, LoginUser loginAdmin) {
        verifyAdminByEmail(loginAdmin);
        verifyMemberByIds(request.getMemberIds());

        Coupon coupon = couponRepository.findByCouponIdLock(request.getCouponId());
        List<IssuedCoupon> issuedCoupons = toIssuedCoupons(request, coupon);
        coupon.issueCoupons(issuedCoupons);

        return CouponIssueResponse.of(issuedCoupons);
    }

    private List<IssuedCoupon> toIssuedCoupons(CouponIssueRequest request, Coupon coupon) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiredAt = issuedAt.plusMonths(1);

        return request.toEntity(issuedAt, expiredAt, IssuedCouponStatus.ACTIVE, coupon);
    }

    private void verifyMemberByIds(List<Long> memberIds) {
        memberIds.forEach(memberService::findMemberById);
    }

    private Admin verifyAdminByEmail(LoginUser loginUser) {
        return adminAuthService.findAdminByEmail(loginUser.getEmail());
    }

    private Coupon createCoupon(CouponRequest request, Long adminId) {
        LocalDateTime createdAt = LocalDateTime.now();
        return request.toEntity(createdAt, CouponStatus.ISSUABLE, adminId);
    }
}
