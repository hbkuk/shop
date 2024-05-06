package com.shop.core.coupon.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.admin.auth.fixture.AdminGithubFixture;
import com.shop.core.coupon.presentation.dto.CouponRequest;
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
import static com.shop.core.notification.step.NotificationSteps.알림_발송_없음_확인;
import static com.shop.core.notification.step.NotificationSteps.알림_발송_확인;

@DisplayName("쿠폰과 알림 인수 테스트")
public class CouponAndNotificationAcceptanceTest extends UserAcceptanceTest {

    @Nested
    class 쿠폰_발급시_알림_발송 {

        String 관리자_깃허브_토큰;

        Member 생성된_첫번째_회원;
        Member 생성된_두번째_회원;

        @BeforeEach
        void 사전_준비() {
            관리자_깃허브_토큰 = 관리자_생성_후_토큰_발급(AdminGithubFixture.황병국);

            생성된_첫번째_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            생성된_두번째_회원 = 회원_생성(윌리엄스.이메일, 윌리엄스.비밀번호, 윌리엄스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

        }

        @Nested
        class 성공 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * And   회원을 생성하고, 토큰을 발급한다.
             * When  생성된 회원에게 쿠폰이 정상적으로 발급된 경우
             * Then  알림이 발송된다.
             */
            @Test
            void 쿠폰_정상_발급_시_알림_발송() {
                // given
                CouponRequest 쿠폰_추가_요청_정보 = CouponRequest.of("여름 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 2);
                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = 쿠폰_추가_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_추가_요청_정보);

                // when
                var 쿠폰_발급할_회원_번호_목록 = CouponIssueRequest.of(List.of(생성된_첫번째_회원.getEmail(), 생성된_두번째_회원.getEmail()));
                성공하는_쿠폰_발급_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_발급할_회원_번호_목록, 쿠폰_추가_요청_응답);
                쿠폰_발급_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답, List.of(생성된_첫번째_회원, 생성된_두번째_회원));

                // then
                알림_발송_확인(관리자_깃허브_토큰, List.of(생성된_첫번째_회원.getEmail(), 생성된_두번째_회원.getEmail()));
            }
        }

        @Nested
        class 실패 {

            /**
             * Given 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 쿠폰을 추가한다.
             * And   회원을 생성하고, 토큰을 발급한다.
             * When  생성된 회원에게 쿠폰이 발급되지 않은 경우
             * Then  알림이 발송되지 않는다.
             */
            @Test
            void 쿠폰_발급_실패_시_알림_미발송() {
                // given
                CouponRequest 쿠폰_추가_요청_정보 = CouponRequest.of("여름 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1);
                ExtractableResponse<Response> 쿠폰_추가_요청_응답 = 쿠폰_추가_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_추가_요청_정보);

                // when
                var 쿠폰_발급할_회원_번호_목록 = CouponIssueRequest.of(List.of(생성된_첫번째_회원.getEmail(), 생성된_두번째_회원.getEmail()));
                실패하는_쿠폰_발급_요청_토큰_포함(관리자_깃허브_토큰, 쿠폰_발급할_회원_번호_목록, 쿠폰_추가_요청_응답);
                쿠폰_미발급_확인(관리자_깃허브_토큰, 쿠폰_추가_요청_응답);

                // then
                알림_발송_없음_확인(관리자_깃허브_토큰);
            }
        }

    }
}
