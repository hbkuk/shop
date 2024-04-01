package com.shop.core.admin.auth.acceptance;

import com.shop.common.util.AcceptanceTest;
import com.shop.common.util.AdminAcceptanceTest;
import com.shop.core.admin.auth.domain.*;
import com.shop.core.admin.auth.fixture.AdminGithubFixture;
import com.shop.core.admin.auth.presentation.dto.AdminGithubCodeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.admin.auth.step.AdminAuthSteps.관리자_토큰_발급_확인;
import static com.shop.core.admin.auth.step.AdminAuthSteps.깃허브_토큰_발급_요청;

@DisplayName("관리자 인증 인수 테스트")
public class AdminAuthAcceptanceTest extends AdminAcceptanceTest {

    @Nested
    class 관리자_깃허브_토큰_발급 {

        @Nested
        class 성공 {

            /**
             * Given 관리자가 등록된다.
             * When  관리자가 깃허브를 통해 토큰을 발급한다.
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 깃허브로_관리자_토큰_발급() {
                // given
                var 관리자_정보 = new Admin(AdminGithubFixture.황병국.email, "010-1234-5678", AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);
                관리자_등록(관리자_정보);

                // when
                var 깃허브_코드_정보 = AdminGithubCodeRequest.of(AdminGithubFixture.황병국.code);
                var 깃허브_토큰_발급_정보 = 깃허브_토큰_발급_요청(깃허브_코드_정보);

                // then
                관리자_토큰_발급_확인(깃허브_토큰_발급_정보);
            }
        }
    }
}
