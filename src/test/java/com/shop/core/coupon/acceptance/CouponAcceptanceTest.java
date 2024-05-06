package com.shop.core.coupon.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.admin.auth.fixture.AdminGithubFixture;
import com.shop.core.coupon.domain.CouponStatus;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.coupon.presentation.dto.CouponStatusRequest;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.shop.core.coupon.step.CouponSteps.*;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.member.fixture.MemberFixture.윌리엄스;

@DisplayName("쿠폰 관련 인수 테스트")
public class CouponAcceptanceTest extends UserAcceptanceTest {

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
                var 관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

                // when
                var 쿠폰_추가_요청_정보 = CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                var 쿠폰_추가_요청_응답 = 쿠폰_추가_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_추가_요청_정보);

                // then
                쿠폰_조회_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, 쿠폰_추가_요청_정보);
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
            void 토큰_없이_쿠폰_추가_실패() {
                // when, then
                var 쿠폰_추가_요청_정보 = CouponRequest.of("여름 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                쿠폰_추가_요청_토큰_미포함(쿠폰_추가_요청_정보);
            }

        }
    }

    @Nested
    class 쿠폰_발급 {

        String 관리자_깃허브_토큰;

        Member 생성된_첫번째_회원;
        Member 생성된_두번째_회원;

        ExtractableResponse<Response> 쿠폰_추가_요청_응답;

        @BeforeEach
        void 사전_준비() {
            관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

            생성된_첫번째_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            생성된_두번째_회원 = 회원_생성(윌리엄스.이메일, 윌리엄스.비밀번호, 윌리엄스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

            CouponRequest 쿠폰_추가_요청_정보 = CouponRequest.of("여름 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
            쿠폰_추가_요청_응답 = 쿠폰_추가_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_추가_요청_정보);

        }

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
                // when
                var 쿠폰_발급할_회원_번호_목록 = CouponIssueRequest.of(List.of(생성된_첫번째_회원.getEmail(), 생성된_두번째_회원.getEmail()));
                성공하는_쿠폰_발급_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_발급할_회원_번호_목록, 쿠폰_추가_요청_응답);

                // then
                쿠폰_발급_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, List.of(생성된_첫번째_회원, 생성된_두번째_회원));
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
                // when, then
                var 쿠폰_발급할_회원_번호_목록 = CouponIssueRequest.of(List.of(생성된_첫번째_회원.getEmail(), 생성된_두번째_회원.getEmail()));
                쿠폰_발급_요청_토큰_미포함(쿠폰_발급할_회원_번호_목록, 쿠폰_추가_요청_응답);
            }
        }
    }

    @Nested
    class 쿠폰_상태_변경 {

        String 관리자_깃허브_토큰;

        Member 생성된_첫번째_회원;
        Member 생성된_두번째_회원;

        ExtractableResponse<Response> 쿠폰_추가_요청_응답;

        @BeforeEach
        void 사전_준비() {
            관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

            생성된_첫번째_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            생성된_두번째_회원 = 회원_생성(윌리엄스.이메일, 윌리엄스.비밀번호, 윌리엄스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

            CouponRequest 쿠폰_추가_요청_정보 = CouponRequest.of("여름 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
            쿠폰_추가_요청_응답 = 쿠폰_추가_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_추가_요청_정보);

        }

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
                // when
                var 쿠폰_상태_변경_정보 = CouponStatusRequest.of(CouponStatus.STOPPED_ISSUANCE);
                쿠폰_상태_변경_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_상태_변경_정보, 쿠폰_추가_요청_응답);

                // then
                쿠폰_상태_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, CouponStatus.STOPPED_ISSUANCE);
            }

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * When  토큰을 통해 쿠폰의 상태를 삭제 상태로 변경한다.
             * Then  정상적으로 쿠폰의 상태가 변경된다.
             */
            @Test
            void 발급_삭제_상태로_변경() {
                // when
                var 쿠폰_상태_변경_정보 = CouponStatusRequest.of(CouponStatus.DELETED);
                쿠폰_상태_변경_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_상태_변경_정보, 쿠폰_추가_요청_응답);

                // then
                쿠폰_상태_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, CouponStatus.DELETED);
            }


        }

        @Nested
        class 실패 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * When  토큰 없이 쿠폰의 상태를 발급 중지 상태로 변경한다.
             * Then  쿠폰의 상태가 변경되지 않는다.
             */
            @Test
            void 토큰_없이_발급_중지_상태_변경() {
                // when
                var 쿠폰_상태_변경_정보 = CouponStatusRequest.of(CouponStatus.STOPPED_ISSUANCE);
                쿠폰_상태_변경_요청_토큰_미포함(쿠폰_상태_변경_정보, 쿠폰_추가_요청_응답);

                // then
                쿠폰_상태_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, CouponStatus.ISSUABLE);
            }

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * When  토큰 없이 쿠폰의 상태를 삭제 상태로 변경한다.
             * Then  쿠폰의 상태가 변경되지 않는다.
             */
            @Test
            void 토큰_없이_삭제_상태_변경() {
                // when
                var 쿠폰_상태_변경_정보 = CouponStatusRequest.of(CouponStatus.DELETED);
                쿠폰_상태_변경_요청_토큰_미포함(쿠폰_상태_변경_정보, 쿠폰_추가_요청_응답);

                // then
                쿠폰_상태_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, CouponStatus.ISSUABLE);
            }
        }
    }
}
