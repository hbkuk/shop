package com.shop.core.member.acceptance;

import com.shop.common.util.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.core.member.fixture.MemberFixture.브라운;
import static com.shop.core.member.step.MemberSteps.*;

@DisplayName("회원 관련 인수 테스트")
class MemberAcceptanceTest extends AcceptanceTest {

    @Nested
    class 회원_생성 {

        @Nested
        class 성공 {

            /**
             * When 회원을 생성한다.
             * Then 회원이 생성된다.
             */
            @Test
            void 회원_생성_성공() {
                // when
                var 생성_요청_응답 = 회원_생성_요청(브라운);

                // then
                회원_생성_확인(생성_요청_응답);
            }
        }

        @Nested
        class 실패 {

            /**
              * Given 회원을 생성한다.
              * When  동일한 이메일로 생성할 경우
              * Then  회원이 생성되지 않는다.
              */
            @Test
            void 회원_생성_실패() {
                // given
                성공하는_회원_생성_요청(브라운.이메일, 브라운.비밀번호, 브라운.나이);

                // when
                var 생성_요청_응답 = 회원_생성_요청(브라운.이메일, 브라운.비밀번호, 브라운.나이);

                // then
                회원_생성_실패_확인(생성_요청_응답);
            }
        }
    }
}