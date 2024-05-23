package com.shop.core.point.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.get_요청_토큰_포함;
import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.memberAuth.step.AuthSteps.회원생성_후_토큰_발급;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                String 정상적인_회원의_토큰 = 회원생성_후_토큰_발급(스미스);

                Map<String, String> 결제_정보 = new HashMap<>();
                결제_정보.put("payment_id", "1234567890");
                결제_정보.put("payment_status", "PAID");

                // when
                ExtractableResponse<Response> 포인트_충전_요청_응답 = post_요청_토큰_미포함("/payment-webhook/points", 결제_정보, HttpStatus.CREATED);

                // then
                ExtractableResponse<Response> 포인트_조회_요청_응답 = get_요청_토큰_포함(정상적인_회원의_토큰, "/points/{id}", getCreatedLocationId(포인트_충전_요청_응답), HttpStatus.OK);

                assertEquals(포인트_조회_요청_응답.jsonPath().getInt("amount"), 10000);
            }

        }

        @Nested
        class 실패 {
            // TODO: 포인트 충전 실패 인수 테스트
        }

    }
}
