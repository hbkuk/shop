package com.shop.core.coupon.step;

import com.shop.core.coupon.domain.CouponStatus;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.coupon.presentation.dto.CouponStatusRequest;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.member.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CouponSteps {

    public static void 쿠폰_조회_요청_토큰_포함(String 토큰_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답, CouponRequest 확인할_쿠폰_정보) {
        ExtractableResponse<Response> response = given().log().all()
                .header("Authorization", 토큰_정보)
                .when()
                .get("/coupons/{id}", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getString("name")).isEqualTo(확인할_쿠폰_정보.getName());
        assertThat(response.jsonPath().getInt("remaining_issue_count")).isEqualTo(확인할_쿠폰_정보.getRemainingIssueCount());
        assertThat(response.jsonPath().getString("coupon_status")).isEqualTo(CouponStatus.ISSUABLE.name());
    }

    public static ExtractableResponse<Response> 쿠폰_추가_요청_토큰_포함(String 토큰_정보, CouponRequest 추가할_쿠폰_정보) {
        return given().log().all()
                .header("Authorization", 토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(추가할_쿠폰_정보)
                .when()
                .post("/coupons")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static void 쿠폰_추가_요청_토큰_미포함(CouponRequest 추가할_쿠폰_정보) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(추가할_쿠폰_정보)
                .when()
                .post("/coupons")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 쿠폰_발급_요청_토큰_포함(String 토큰_정보, CouponIssueRequest 쿠폰_발급할_회원_번호_목록, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        given().log().all()
                .header("Authorization", 토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(쿠폰_발급할_회원_번호_목록)
                .when()
                .post("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 쿠폰_발급_확인(String 토큰_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답, List<Member> 확인할_회원_정보) {
        ExtractableResponse<Response> 발급된_쿠폰_조회_응답 = given().log().all()
                .header("Authorization", 토큰_정보)
                .when()
                .get("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Long> 발급된_쿠폰의_회원_번호 = 발급된_쿠폰_조회_응답.jsonPath().getList("issued_member_ids", Long.class);
        List<Long> 회원_번호 = 확인할_회원_정보.stream().map(Member::getId).collect(Collectors.toList());


        assertThat(발급된_쿠폰의_회원_번호).isEqualTo(회원_번호);
    }

    public static void 쿠폰_발급_요청_토큰_미포함(CouponIssueRequest 쿠폰_발급할_회원_번호_목록, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(쿠폰_발급할_회원_번호_목록)
                .when()
                .post("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 쿠폰_상태_변경_요청_토큰_포함(String 토큰_정보, CouponStatusRequest 변경할_쿠폰_상태_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        ExtractableResponse<Response> 쿠폰_상태_변경_요청_응답 = given().log().all()
                .header("Authorization", 토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(변경할_쿠폰_상태_정보)
                .when()
                .put("/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 쿠폰_상태_변경_요청_토큰_미포함(CouponStatusRequest 변경할_쿠폰_상태_정보, ExtractableResponse<Response> 쿠폰_추가_요청_응답) {
        ExtractableResponse<Response> 쿠폰_상태_변경_요청_응답 = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(변경할_쿠폰_상태_정보)
                .when()
                .put("/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 쿠폰_상태_확인(String 관리자_깃허브_토큰, ExtractableResponse<Response> 쿠폰_추가_요청_응답, CouponStatus couponStatus) {
        ExtractableResponse<Response> 쿠폰_조회_요청_응답 = given().log().all()
                .header("Authorization", 관리자_깃허브_토큰)
                .when()
                .get("/coupons/{coupondId}", getCreatedLocationId(쿠폰_추가_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(쿠폰_조회_요청_응답.jsonPath().getString("coupon_status")).isEqualTo(couponStatus.name());
    }
}
