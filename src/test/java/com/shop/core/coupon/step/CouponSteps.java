package com.shop.core.coupon.step;

import com.shop.core.coupon.domain.CouponStatus;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.coupon.presentation.dto.CouponStatusRequest;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.member.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CouponSteps {

    public static void 쿠폰_조회_요청_토큰_포함(String 토큰_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답, CouponRequest 확인할_쿠폰_정보) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰_정보, "/coupons/{id}", getCreatedLocationId(쿠폰_추가_요청_응답), HttpStatus.OK);

        assertThat(응답.jsonPath().getString("name")).isEqualTo(확인할_쿠폰_정보.getName());
        assertThat(응답.jsonPath().getInt("remaining_issue_count")).isEqualTo(확인할_쿠폰_정보.getRemainingIssueCount());
        assertThat(응답.jsonPath().getString("coupon_status")).isEqualTo(CouponStatus.ISSUABLE.name());
    }

    public static ExtractableResponse<Response> 쿠폰_추가_요청_토큰_포함(String 토큰_정보, CouponRequest 추가할_쿠폰_정보) {
        return post_요청_토큰_포함(토큰_정보, "/coupons", 추가할_쿠폰_정보, HttpStatus.CREATED);
    }

    public static void 쿠폰_추가_요청_토큰_미포함(CouponRequest 추가할_쿠폰_정보) {
        post_요청_토큰_미포함("/coupons", 추가할_쿠폰_정보, HttpStatus.UNAUTHORIZED);
    }

    public static void 성공하는_쿠폰_발급_요청_토큰_포함(String 토큰_정보, CouponIssueRequest 쿠폰_발급할_회원_번호_목록, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        post_요청_토큰_포함(토큰_정보, "/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답), 쿠폰_발급할_회원_번호_목록, HttpStatus.OK);
    }

    public static void 실패하는_쿠폰_발급_요청_토큰_포함(String 토큰_정보, CouponIssueRequest 쿠폰_발급할_회원_번호_목록, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        post_요청_토큰_포함(토큰_정보, "/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답), 쿠폰_발급할_회원_번호_목록, HttpStatus.BAD_REQUEST);
    }

    public static void 쿠폰_발급_확인(String 토큰_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답, List<Member> 확인할_회원_정보) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰_정보, "/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답), HttpStatus.OK);

        List<String> 발급된_쿠폰의_회원_번호 = 응답.jsonPath().getList("issued_member_emails", String.class);
        List<String> 회원_이메일 = 확인할_회원_정보.stream().map(Member::getEmail).collect(Collectors.toList());

        assertThat(발급된_쿠폰의_회원_번호).containsAnyElementsOf(회원_이메일);
    }

    public static void 쿠폰_미발급_확인(String 토큰_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰_정보, "/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답), HttpStatus.OK);

        List<String> 발급된_쿠폰의_회원_번호 = 응답.jsonPath().getList("issued_member_emails", String.class);
        assertThat(발급된_쿠폰의_회원_번호).isEmpty();
    }

    public static void 쿠폰_발급_요청_토큰_미포함(CouponIssueRequest 쿠폰_발급할_회원_번호_목록, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        post_요청_토큰_미포함("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답), 쿠폰_발급할_회원_번호_목록, HttpStatus.UNAUTHORIZED);
    }

    public static void 쿠폰_상태_변경_요청_토큰_포함(String 토큰_정보, CouponStatusRequest 변경할_쿠폰_상태_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        put_요청_토큰_포함(토큰_정보, "/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답), 변경할_쿠폰_상태_정보, HttpStatus.OK);
    }

    public static void 쿠폰_상태_변경_요청_토큰_미포함(CouponStatusRequest 변경할_쿠폰_상태_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        put_요청_토큰_미포함("/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답), 변경할_쿠폰_상태_정보, HttpStatus.UNAUTHORIZED);
    }

    public static void 쿠폰_상태_확인(String 관리자_깃허브_토큰, ExtractableResponse<Response> 쿠폰_추가_요청_응답, CouponStatus couponStatus) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(관리자_깃허브_토큰, "/coupons/{coupondId}", getCreatedLocationId(쿠폰_추가_요청_응답), HttpStatus.OK);

        assertThat(응답.jsonPath().getString("coupon_status")).isEqualTo(couponStatus.name());
    }
}
