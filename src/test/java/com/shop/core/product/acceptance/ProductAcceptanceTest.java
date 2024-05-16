package com.shop.core.product.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.store.presentation.dto.StoreRequest;
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
import static com.shop.core.store.step.StoreSteps.성공하는_상점_등록_요청_토큰_포함;
import static com.shop.core.storeManager.fixture.StoreManagerFixture.최상점;
import static com.shop.core.storeManagerAuth.step.StoreManagerSteps.상점_관리자_생성_후_토큰_발급;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends UserAcceptanceTest {

    @Nested
    class 상품_등록 {


        @Nested
        class 성공 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * When  토큰을 통해 상품을 등록한다.
             * Then  정상적으로 상품이 등록된다.
             */
            @Test
            void 토큰_포함_상품_등록() {
                // given
                String 상점_관리자_토큰 = 상점_관리자_생성_후_토큰_발급(최상점);

                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(상점_관리자_토큰, 상점_등록_정보);

                // when
                Map<String, String> 상품_등록_정보 = new HashMap<>();
                상품_등록_정보.put("name", "블링블링 청바지");
                상품_등록_정보.put("sales_count", "100");
                상품_등록_정보.put("description", "블링블링한 멋진 바지를 입어보세요~");
                상품_등록_정보.put("price", "30000");
                상품_등록_정보.put("store_id", getCreatedLocationId(상점_등록_요청_응답).toString());

                ExtractableResponse<Response> 상품_등록_요청_응답 = post_요청_토큰_포함(상점_관리자_토큰, "/products", 상품_등록_정보, HttpStatus.CREATED);

                // then
                get_요청_토큰_포함(상점_관리자_토큰, "/products/{id}", getCreatedLocationId(상품_등록_요청_응답), HttpStatus.OK);
            }

        }

        @Nested
        class 실패 {

            /**
             * Given 상점 관리자를 생성한다.
             * When  토큰 없이 상품을 등록할 경우
             * Then  상품이 등록되지 않는다.
             */
            @Test
            void 토큰_없이_상품_등록() {
                // given
                String 상점_관리자_토큰 = 상점_관리자_생성_후_토큰_발급(최상점);

                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(상점_관리자_토큰, 상점_등록_정보);

                // when
                Map<String, String> 상품_등록_정보 = new HashMap<>();
                상품_등록_정보.put("name", "블링블링 청바지");
                상품_등록_정보.put("sales_count", "100");
                상품_등록_정보.put("description", "블링블링한 멋진 바지를 입어보세요~");
                상품_등록_정보.put("price", "30000");
                상품_등록_정보.put("store_id", getCreatedLocationId(상점_등록_요청_응답).toString());

                post_요청_토큰_미포함("/products", 상품_등록_정보, HttpStatus.UNAUTHORIZED);
            }
        }

    }
}
