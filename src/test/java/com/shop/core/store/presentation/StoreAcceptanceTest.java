package com.shop.core.store.presentation;

import com.shop.common.util.AcceptanceTest;
import com.shop.core.store.domain.StoreStatus;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.*;
import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static com.shop.core.storeManager.step.StoreManagerSteps.상점_관리자_생성_요청;
import static com.shop.core.storeManagerAuth.step.StoreManagerSteps.성공하는_상점_관리자_토큰_발급_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상점 관련 인수 테스트")
public class StoreAcceptanceTest extends AcceptanceTest {

    @Nested
    class 상점_등록 {

        @Nested
        class 성공 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * When  토큰을 통해 상점을 등록한다.
             * Then  정상적으로 상점이 등록된다.
             */
            @Test
            void 상점_등록() {
                // given
                상점_관리자_생성_요청(김상점);
                String 발급된_토큰 = "Bearer " + 성공하는_상점_관리자_토큰_발급_요청(김상점);

                // when
                Map<String, String> 상점_등록_정보 = new HashMap<>();
                상점_등록_정보.put("name", "패션 팔래트");
                상점_등록_정보.put("content", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");

                ExtractableResponse<Response> 상점_등록_요청_응답 = post_요청_토큰_포함(발급된_토큰, "/store", 상점_등록_정보, HttpStatus.CREATED);

                // then
                ExtractableResponse<Response> 상점_조회_요청_응답 = get_요청_토큰_포함(발급된_토큰, "/store/{id}", getCreatedLocationId(상점_등록_요청_응답), HttpStatus.OK);

                assertThat(상점_조회_요청_응답.jsonPath().getString("name")).isEqualTo(상점_등록_정보.get("name"));
            }

        }

        @Nested
        class 실패 {

            /**
             * When  토큰 없이 상품을 등록할 경우
             * Then  상품이 등록되지 않는다.
             */
            @Test
            void 토큰_없이_상품_등록() {
                // when
                Map<String, String> 상점_등록_정보 = new HashMap<>();
                상점_등록_정보.put("name", "패션 팔래트");
                상점_등록_정보.put("content", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");

                post_요청_토큰_미포함("/store", 상점_등록_정보, HttpStatus.UNAUTHORIZED);
            }
        }

    }

    @Nested
    class 상점_상태_변경 {

        @Nested
        class 성공 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 상점을 등록한다.
             * When  토큰을 통해 상점의 상태를 변경한다.
             * Then  정상적으로 상점의 상태가 변경된다.
             */
            @Test
            void 상점_상태_변경() {
                // given
                상점_관리자_생성_요청(김상점);
                String 발급된_토큰 = "Bearer " + 성공하는_상점_관리자_토큰_발급_요청(김상점);

                // when
                Map<String, String> 상점_등록_정보 = new HashMap<>();
                상점_등록_정보.put("name", "패션 팔래트");
                상점_등록_정보.put("content", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");

                ExtractableResponse<Response> 상점_등록_요청_응답 = post_요청_토큰_포함(발급된_토큰, "/store", 상점_등록_정보, HttpStatus.CREATED);

                // when
                Map<String, String> 상점_변경_정보 = new HashMap<>();
                상점_변경_정보.put("status", "UNDER_MAINTENANCE");

                put_요청_토큰_포함(발급된_토큰, "/store/{storeId}", getCreatedLocationId(상점_등록_요청_응답), 상점_변경_정보, HttpStatus.OK);

                // then
                ExtractableResponse<Response> 상점_조회_요청_응답 = get_요청_토큰_포함(발급된_토큰, "/store/{id}", getCreatedLocationId(상점_등록_요청_응답), HttpStatus.OK);

                assertThat(상점_조회_요청_응답.jsonPath().getString("status")).isEqualTo(상점_변경_정보.get("status"));
            }

        }

        @Nested
        class 실패 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 상점을 등록한다.
             * When  토큰 없이 상점의 상태를 변경할 경우
             * Then  상점의 상태가 변경되지 않는다.
             */
            @Test
            void 토큰_없이_상점_상태_변경() {
                // given
                상점_관리자_생성_요청(김상점);
                String 발급된_토큰 = "Bearer " + 성공하는_상점_관리자_토큰_발급_요청(김상점);

                // when
                Map<String, String> 상점_등록_정보 = new HashMap<>();
                상점_등록_정보.put("name", "패션 팔래트");
                상점_등록_정보.put("content", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");

                ExtractableResponse<Response> 상점_등록_요청_응답 = post_요청_토큰_포함(발급된_토큰, "/store", 상점_등록_정보, HttpStatus.CREATED);

                // when
                Map<String, String> 상점_변경_정보 = new HashMap<>();
                상점_변경_정보.put("status", "UNDER_MAINTENANCE");

                put_요청_토큰_미포함("/store/{storeId}", getCreatedLocationId(상점_등록_요청_응답), 상점_변경_정보, HttpStatus.UNAUTHORIZED);

                // then
                ExtractableResponse<Response> 상점_조회_요청_응답 = get_요청_토큰_포함(발급된_토큰, "/store/{id}", getCreatedLocationId(상점_등록_요청_응답), HttpStatus.OK);

                assertThat(상점_조회_요청_응답.jsonPath().getString("status")).isEqualTo(StoreStatus.OPEN.toString());
            }
        }
    }
}
