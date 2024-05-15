package com.shop.core.store.presentation;

import com.shop.common.util.AcceptanceTest;
import com.shop.core.store.presentation.dto.StoreRequest;
import com.shop.core.store.presentation.dto.StoreStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.core.store.domain.StoreStatus.UNDER_MAINTENANCE;
import static com.shop.core.store.step.StoreSteps.*;
import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static com.shop.core.storeManagerAuth.step.StoreManagerSteps.상점_관리자_생성_후_토큰_발급;

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
                var 발급된_상점_관리자_토큰 = 상점_관리자_생성_후_토큰_발급(김상점);

                // when
                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다."); // TODO: Fixture 분리
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(발급된_상점_관리자_토큰, 상점_등록_정보);

                // then
                상점_정상_등록_확인(발급된_상점_관리자_토큰, 상점_등록_요청_응답, 상점_등록_정보);
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
                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                실패하는_상점_등록_요청_토큰_미포함(상점_등록_정보);
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
                var 발급된_토큰 = 상점_관리자_생성_후_토큰_발급(김상점);

                // when
                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(발급된_토큰, 상점_등록_정보);

                // when
                var 상점_변경_정보 = StoreStatusRequest.of(UNDER_MAINTENANCE); // TODO: Fixture 분리
                성공하는_상점_변경_요청_토큰_포함(발급된_토큰, 상점_등록_요청_응답, 상점_변경_정보);

                // then
                상점_상태_정상_변경_확인(발급된_토큰, 상점_등록_요청_응답, 상점_변경_정보);
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
                var 발급된_토큰 = 상점_관리자_생성_후_토큰_발급(김상점);

                // when
                var 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                var 상점_등록_요청_응답 = 성공하는_상점_등록_요청_토큰_포함(발급된_토큰, 상점_등록_정보);

                // when, then
                var 상점_변경_정보 = StoreStatusRequest.of(UNDER_MAINTENANCE);
                실패하는_상점_변경_요청_토큰_미포함(상점_등록_요청_응답, 상점_변경_정보);
            }
        }
    }
}
