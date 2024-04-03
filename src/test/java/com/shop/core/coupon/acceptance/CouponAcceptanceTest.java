package com.shop.core.coupon.acceptance;

import com.shop.common.util.AdminAcceptanceTest;
import com.shop.core.admin.auth.fixture.AdminGithubFixture;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("쿠폰 관련 인수 테스트")
public class CouponAcceptanceTest extends AdminAcceptanceTest {

    @Nested
    class 쿠폰_추가 {


        @Nested
        class 성공 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * When  토큰을 통해 쿠폰을 추가한다.
             * Then  정상적으로 쿠폰이 추가된다.
             */
            @Test
            void 쿠폰_추가_성공() {
                // given
                var 깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                // when
                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = given().log().all()
                        .header("Authorization", 깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // then
                ExtractableResponse<Response> 쿠폰_조회_응답 = given().log().all()
                        .header("Authorization", 깃허브_토큰)
                        .when()
                        .get("/coupons/{id}", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();
            }
        }


        @Nested
        class 실패 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * When  토큰 없이 쿠폰을 추가할 경우
             * Then  쿠폰이 등록되지 않는다.
             */
            @Test
            void 쿠폰_추가_성공() {
                // when
                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();
            }

        }

    }

    @Nested
    class 쿠폰_발급 {

        @Nested
        class 성공 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * And   회원을 생성하고, 토큰을 발급한다.
             * When  생성된 회원에게 추가한 쿠폰을 발급한다.
             * Then  정상적으로 쿠폰이 발급된다.
             */
            @Test
            void 쿠폰_발급_성공() {
                // given
                var 관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // when
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                CouponIssueRequest 쿠폰_발급할_회원_번호_목록 = CouponIssueRequest.of(List.of(생성된_회원.getId()));

                given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_발급할_회원_번호_목록)
                        .when()
                        .post("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                // then
                ExtractableResponse<Response> 발급된_쿠폰_조회_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .when()
                        .get("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                List<Long> 발급된_쿠폰의_회원_번호 = 발급된_쿠폰_조회_응답.jsonPath().getList("issuedMemberIds", Long.class);
                assertThat(발급된_쿠폰의_회원_번호).containsExactlyInAnyOrder(생성된_회원.getId());
            }

        }

        @Nested
        class 실패 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * And   회원을 생성하고, 토큰을 발급한다.
             * When  생성된 회원에게 관리자 토큰없이, 추가한 쿠폰을 발급한다.
             * Then  쿠폰이 발급되지 않는다.
             */
            @Test
            void 관리자_토큰_없이_쿠폰_발급() {
                // given
                var 관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                // when
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                CouponIssueRequest 쿠폰_발급할_회원_번호_목록 = CouponIssueRequest.of(List.of(생성된_회원.getId()));

                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_발급할_회원_번호_목록)
                        .when()
                        .post("/coupons/{couponId}/issue", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();
            }
        }
    }

    @Nested
    class 쿠폰_상태_변경 {

        @Nested
        class 성공 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * When  토큰을 통해 쿠폰의 상태를 발급 중지 상태로 변경한다.
             * Then  정상적으로 쿠폰의 상태가 변경된다.
             */
            @Test
            void 발급_중지_상태로_변경() {
                // given
                var 관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                Map<String, String> 변경할_쿠폰_상태_정보 = new HashMap<>();
                변경할_쿠폰_상태_정보.put("coupon_status", "STOPPED_ISSUANCE");

                ExtractableResponse<Response> 쿠폰_상태_변경_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(변경할_쿠폰_상태_정보)
                        .when()
                        .put("/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                ExtractableResponse<Response> 쿠폰_조회_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .when()
                        .get("/coupons/{coupondId}", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(쿠폰_조회_요청_응답.jsonPath().getString("coupon_status")).isEqualTo("STOPPED_ISSUANCE");
            }


            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * When  토큰을 통해 쿠폰의 상태를 삭제 상태로 변경한다.
             * Then  정상적으로 쿠폰의 상태가 변경된다.
             */
            @Test
            void 발급_삭제_상태로_변경() {
                // given
                var 관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                Map<String, String> 변경할_쿠폰_상태_정보 = new HashMap<>();
                변경할_쿠폰_상태_정보.put("coupon_status", "DELETED");

                ExtractableResponse<Response> 쿠폰_상태_변경_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(변경할_쿠폰_상태_정보)
                        .when()
                        .put("/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                ExtractableResponse<Response> 쿠폰_조회_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .when()
                        .get("/coupons/{coupondId}", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(쿠폰_조회_요청_응답.jsonPath().getString("coupon_status")).isEqualTo("DELETED");
            }


        }

        @Nested
        class 실패 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * When  토큰 없이 쿠폰의 상태를 발급중지 상태로 변경한다.
             * Then  쿠폰의 상태가 변경되지 않는다.
             */
            @Test
            void 토큰_없이_상태_변경() {
                // given
                var 관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                Map<String, String> 쿠폰_정보 = new HashMap<>();
                쿠폰_정보.put("name", "봄 맞이 특별 쿠폰");
                쿠폰_정보.put("description", "인기 브랜드의 다양한 제품 할인");
                쿠폰_정보.put("max_discount_amount", "30000");
                쿠폰_정보.put("discount_amount", "10");
                쿠폰_정보.put("remaining_issue_count", "100");

                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(쿠폰_정보)
                        .when()
                        .post("/coupons")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract();

                Map<String, String> 변경할_쿠폰_상태_정보 = new HashMap<>();
                변경할_쿠폰_상태_정보.put("coupon_status", "STOPPED_ISSUANCE");

                ExtractableResponse<Response> 쿠폰_상태_변경_요청_응답 = given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(변경할_쿠폰_상태_정보)
                        .when()
                        .put("/coupons/{couponId}/status", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                ExtractableResponse<Response> 쿠폰_조회_요청_응답 = given().log().all()
                        .header("Authorization", 관리자_깃허브_토큰)
                        .when()
                        .get("/coupons/{coupondId}", getCreatedLocationId(쿠폰_추가_요청_응답))
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(쿠폰_조회_요청_응답.jsonPath().getString("coupon_status")).isEqualTo("ISSUABLE");
            }

        }
    }
}
