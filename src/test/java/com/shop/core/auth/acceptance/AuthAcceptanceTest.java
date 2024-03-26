package com.shop.core.auth.acceptance;

import com.shop.common.util.AcceptanceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.shop.core.auth.step.AuthSteps.*;
import static com.shop.core.member.fixture.MemberFixture.윌리엄스;
import static com.shop.core.member.fixture.MemberFixture.존슨;
import static com.shop.core.member.step.MemberSteps.회원_생성_요청;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Nested
    class 토큰_발급 {
        @Nested
        class 성공 {
            /**
             * Given 회원을 생선한다.
             * When  생성된 회원 정보로 토큰 발급을 요청할 경우
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 저장된_회원으로_토큰_발급_요청() {
                // given
                회원_생성_요청(윌리엄스);

                // when
                var 발급된_토큰 = 성공하는_토큰_발급_요청(윌리엄스);

                // then
                토큰_확인(발급된_토큰);
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 비밀번호가 아닌 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 비밀번호가_다른_회원의_토큰_발급_요청() {
                // given
                회원_생성_요청(존슨);

                // then
                실패하는_토큰_발급_요청(존슨, "Changed Password");
            }

            /**
             * When  회원 저장소에 존재하지 않는 회원 정보로 토큰 발급을 요청할 경우
             * Then  토큰이 발급되지 않는다.
             */
            @Test
            void 존재하지_않는_회원의_토큰_발급_요청() {
                // when, then
                실패하는_토큰_발급_요청(윌리엄스);
            }
        }
    }
}
