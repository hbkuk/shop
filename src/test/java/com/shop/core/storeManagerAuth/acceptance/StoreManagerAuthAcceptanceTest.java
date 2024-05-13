package com.shop.core.storeManagerAuth.acceptance;

import com.shop.common.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static com.shop.core.storeManager.step.StoreManagerSteps.상점_관리자_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상점 관리자 인증 인수 테스트")
public class StoreManagerAuthAcceptanceTest extends AcceptanceTest {

    @Nested
    class 토큰_발급 {

        @Nested
        class 성공 {

            /**
             * Given 상점 관리자를 생성한다.
             * When  생성된 상점 관리자 정보로 토큰 발급을 요청할 경우
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 상점_관리자_토큰_발급_요청() {
                // given
                상점_관리자_생성_요청(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);

                // when
                Map<String, String> params = new HashMap<>();
                params.put("email", 김상점.이메일);
                params.put("password", 김상점.비밀번호);

                ExtractableResponse<Response> 응답 = post_요청_토큰_미포함("/login/store-manager", params, HttpStatus.OK);

                // then
                assertThat(응답.jsonPath().getString("access_token")).isNotBlank();
            }

        }

        @Nested
        class 실패 {

            /**
             * Given 상점 관리자를 생성한다.
             * When  생성된 상점 관리자의 비밀번호가 아닌 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 비밀번호가_다른_상점_관리자_토큰_발급_요청() {
                // given
                상점_관리자_생성_요청(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);

                // when, then
                Map<String, String> params = new HashMap<>();
                params.put("email", 김상점.이메일);
                params.put("password", "Changed Password" + 김상점.비밀번호);

                ExtractableResponse<Response> 응답 = post_요청_토큰_미포함("/login/store-manager", params, HttpStatus.BAD_REQUEST);
            }

            /**
             * When  존재하지 않는 상점 관리자 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 존재하지_않는_상점_관리자_토큰_발급_요청() {
                // when, then
                Map<String, String> params = new HashMap<>();
                params.put("email", 김상점.이메일);
                params.put("password", 김상점.비밀번호);

                ExtractableResponse<Response> 응답 = post_요청_토큰_미포함("/login/store-manager", params, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
