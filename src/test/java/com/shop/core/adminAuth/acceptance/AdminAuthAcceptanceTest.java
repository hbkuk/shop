package com.shop.core.adminAuth.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.adminAuth.domain.Admin;
import com.shop.core.adminAuth.domain.AdminRole;
import com.shop.core.adminAuth.domain.AdminSignupChannel;
import com.shop.core.adminAuth.domain.AdminStatus;
import com.shop.core.adminAuth.fixture.AdminGithubFixture;
import com.shop.core.adminAuth.presentation.dto.AdminGithubCodeRequest;
import com.shop.core.adminAuth.step.AdminAuthSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("관리자 인증 인수 테스트")
public class AdminAuthAcceptanceTest extends UserAcceptanceTest {

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
                var 깃허브_토큰_발급_정보 = AdminAuthSteps.깃허브_토큰_발급_요청(깃허브_코드_정보);

                // then
                AdminAuthSteps.관리자_토큰_발급_확인(깃허브_토큰_발급_정보);
            }
        }
    }
}
