package com.shop.core.coupon.presentation;

import com.shop.core.auth.domain.LoginUser;
import com.shop.core.auth.presentation.AuthenticationPrincipal;
import com.shop.core.coupon.application.CouponService;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.coupon.presentation.dto.CouponResponse;
import com.shop.core.coupon.presentation.dto.CouponStatusRequest;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/coupons")
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest request,
                                                       @AuthenticationPrincipal LoginUser loginUser) {
        CouponResponse response = couponService.createCoupon(request, loginUser);
        return ResponseEntity.created(URI.create("/coupons/" + response.getId())).build();
    }

    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<CouponResponse> findById(@PathVariable Long couponId,
                                                   @AuthenticationPrincipal LoginUser loginUser) {
        CouponResponse response = couponService.findById(couponId, loginUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/coupons/{couponId}/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(@PathVariable Long couponId,
                                                           @RequestBody CouponIssueRequest request,
                                                           @AuthenticationPrincipal LoginUser loginAdmin) {
        CouponIssueResponse response = couponService.issueCoupon(CouponIssueRequest.mergeCouponId(couponId, request), loginAdmin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/coupons/{couponId}/issue")
    public ResponseEntity<CouponIssueResponse> findIssueCoupons(@PathVariable Long couponId,
                                                                @AuthenticationPrincipal LoginUser loginAdmin) {
        CouponIssueResponse issueCoupons = couponService.findIssueCoupons(couponId, loginAdmin);
        return ResponseEntity.ok(issueCoupons);
    }

    @PutMapping("/coupons/{couponId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long couponId,
                                             @RequestBody CouponStatusRequest request,
                                             @AuthenticationPrincipal LoginUser loginAdmin) {
        couponService.updateStatus(CouponStatusRequest.mergeCouponId(couponId, request), loginAdmin);
        return ResponseEntity.ok().build();
    }
}
