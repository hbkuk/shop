package com.shop.core.adminAuth.application;

import com.shop.common.annotation.ComponentTest;
import com.shop.core.adminAuth.application.dto.AdminGithubProfileResponse;
import com.shop.core.adminAuth.fixture.AdminGithubFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentTest
@DisplayName("깃허브 클라이언트 컴포넌트 테스트")
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    @Nested
    class 깃허브_코드로_깃허브_토큰_발급 {

        /**
         * When  깃허브로 발급받은 코드로 토큰 발급을 요청한다.
         * Then  정상적으로 토큰이 발급된다.
         */
        @Test
        void 토큰_발급_요청() {
            // when
            String 코드 = AdminGithubFixture.황병국.code;

            String 깃허브_토큰 = githubClient.requestGithubToken(코드);

            // then
            assertThat(깃허브_토큰).isNotNull();
        }
    }

    @Nested
    class 깃허브_토큰으로_프로필_정보_요청 {

        /**
         * Given 깃허브로 발급받은 코드로 토큰 발급을 요청한다.
         * When  깃허브 토큰을 통해 프로필 정보를 요청한다.
         * Then  프로필 정보를 확인할 수 있다.
         */
        @Test
        void 프로필_정보_요청() {
            // given
            String 코드 = AdminGithubFixture.황병국.code;
            String 깃허브_토큰 = githubClient.requestGithubToken(코드);

            // when
            AdminGithubProfileResponse response = githubClient.requestGithubProfile(깃허브_토큰);

            // then
            assertThat(response).isNotNull();
        }
    }
}
