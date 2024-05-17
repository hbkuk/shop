package com.shop.core.product.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.product.presentation.dto.ProductRequest;
import com.shop.core.store.presentation.dto.StoreRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.core.product.step.ProductSteps.*;
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
                var 상점_관리자_토큰 = 상점_관리자_생성_후_토큰_발급(최상점);

                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(상점_관리자_토큰, 상점_등록_정보);

                // when
                var 상품_등록_정보 = ProductRequest.of("블링블링 청바지", 100, "블링블링한 멋진 바지를 입어보세요~", 30000, getCreatedLocationId(상점_등록_요청_응답));
                var 상품_등록_요청_응답 = 성공하는_상품_등록_요청(상점_관리자_토큰, 상품_등록_정보);

                // then
                상품_정상_등록_확인(상점_관리자_토큰, 상품_등록_요청_응답);
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
                var 상점_관리자_토큰 = 상점_관리자_생성_후_토큰_발급(최상점);

                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(상점_관리자_토큰, 상점_등록_정보);

                // when, then
                var 상품_등록_정보 = ProductRequest.of("블링블링 청바지", 100, "블링블링한 멋진 바지를 입어보세요~", 30000, getCreatedLocationId(상점_등록_요청_응답));
                실패하는_상품_등록_요청(상품_등록_정보);
            }
        }

    }
}
