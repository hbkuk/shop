package com.shop.core.point.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.point.domain.PaymentStatus;
import com.shop.core.point.presentation.dto.PaymentWebhookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.memberAuth.step.AuthSteps.회원생성_후_토큰_발급;
import static com.shop.core.point.fixture.PaymentFixture.첫번째_결제_정보;
import static com.shop.core.point.step.PointStep.웹훅_포인트_충전_요청;
import static com.shop.core.point.step.PointStep.포인트_충전_확인;

@DisplayName("포인트 관련 인수 테스트")
public class PointAcceptanceTest extends UserAcceptanceTest {

    @Nested
    class 포인트_충전 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성하고, 토큰을 발급한다.
             * When  포인트 충전 요청을 한다.
             * Then  정상적으로 포인트가 충전된다.
             */
            @Test
            void 포인트_충전() {
                // given
                var 회원_토큰 = 회원생성_후_토큰_발급(스미스);
                var 결제_정보 = PaymentWebhookRequest.of(첫번째_결제_정보.결제_번호, PaymentStatus.PAID);

                // when
                var 포인트_충전_요청_응답 = 웹훅_포인트_충전_요청(결제_정보);

                // then
                포인트_충전_확인(회원_토큰, 포인트_충전_요청_응답);
            }
        }
    }
}
