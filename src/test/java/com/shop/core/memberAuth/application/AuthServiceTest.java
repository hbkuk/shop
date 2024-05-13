package com.shop.core.memberAuth.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.member.exception.PasswordMismatchException;
import com.shop.core.memberAuth.application.dto.AuthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.member.fixture.MemberFixture.브라운;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("인증 서비스 레이어 테스트")
public class AuthServiceTest extends ApplicationTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberService memberService;

    @Nested
    class 토큰_발급 {

        @Nested
        class 성공 {

            /**
             * Given  회원을 생성한다.
             * When   생성시 사용했던 이메일과 비밀번호로 토큰 발급을 요청할 경우
             * Then   정상적으로 토큰이 발급된다.
             */
            @Test
            void 토큰_발급_요청() {
                // given
                MemberRequest 브라운_회원_생성_요청 = MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                memberService.createMember(브라운_회원_생성_요청);

                // when
                AuthResponse authResponse = authService.createToken(브라운.이메일, 브라운.비밀번호);

                // then
                assertThat(authResponse.getAccessToken()).isNotBlank();
            }
        }

        @Nested
        class 실패 {

            /**
             * Given  회원을 생성한다.
             * When   생성시 사용했던 비밀번호가 아닌 다른 비밀번호로 토큰 발급을 요청할 경우
             * Then   토큰이 발급되지 않는다.
             */
            @Test
            void 토큰_발급_요청() {
                // given
                MemberRequest 브라운_회원_생성_요청 = MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                memberService.createMember(브라운_회원_생성_요청);

                // when, then
                assertThatExceptionOfType(PasswordMismatchException.class)
                        .isThrownBy(() -> {
                            authService.createToken(브라운.이메일, "different" + 브라운.비밀번호);
                        })
                        .withMessageMatching(ErrorType.PASSWORD_MISMATCH.getMessage());
            }
        }
    }
}
