package com.shop.core.storeManager.acceptance;

import com.shop.common.util.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static com.shop.core.storeManager.step.StoreManagerSteps.*;

@DisplayName("상점 관리자 관련 인수 테스트")
public class StoreManagerAcceptanceTest extends AcceptanceTest {

    @Nested
    class 상점_관리자_생성 {

        @Nested
        class 성공 {

            /**
             * When 상점 관리자를 생성한다.
             * Then 상점 관리자가 생성된다.
             */
            @Test
            void 상점_관리자_생성_성공() {
                // when
                var 생성_요청_응답 = 상점_관리자_생성_요청(김상점);

                // then
                상점_관리자_생성_확인(생성_요청_응답);
            }
        }

        @Nested
        class 실패 {

            /**
             * Given 상점 관리자를 생성한다.
             * When  동일한 이메일로 생성할 경우
             * Then  상점 관리자가 생성되지 않는다.
             */
            @Test
            void 상점_관리자_생성_실패() {
                // given
                성공하는_상점_관리자_생성_요청(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);

                // when
                var 생성_요청_응답 = 상점_관리자_생성_요청(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);

                // then
                상점_관리자_생성_실패_확인(생성_요청_응답);
            }
        }

    }
}
