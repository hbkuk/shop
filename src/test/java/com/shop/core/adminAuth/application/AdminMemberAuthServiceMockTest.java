package com.shop.core.adminAuth.application;

import com.shop.common.auth.JwtTokenProvider;
import com.shop.common.exception.ErrorType;
import com.shop.core.adminAuth.application.dto.AdminGithubProfileResponse;
import com.shop.core.adminAuth.domain.*;
import com.shop.core.adminAuth.exception.NonMatchingSignupChannelException;
import com.shop.core.adminAuth.exception.NotFoundAdminException;
import com.shop.core.adminAuth.fixture.AdminGithubFixture;
import com.shop.core.adminAuth.presentation.dto.AdminTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@DisplayName("관리자 인증 서비스 레이어 테스트")
@ExtendWith(MockitoExtension.class)
public class AdminMemberAuthServiceMockTest {

    AdminAuthService adminAuthService;

    @Mock
    AdminRepository adminRepository;

    @Mock
    GithubClient githubClient;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void 사전_관리자_인증_서비스_생성() {
        adminAuthService = new AdminAuthService(adminRepository, githubClient, jwtTokenProvider);
    }

    @Nested
    class 깃허브로_관리자_토큰_발급 {

        @Nested
        class 성공 {

            /**
             * Given 깃허브로 회원가입된 관리자를 생성한다.
             * When  깃허브를 통해 발급된 코드(Code)로 깃허브 토큰 발급을 요청한다.
             * And   깃허브 토큰을 통해 깃허브 프로필 정보를 요청한다.
             * And   깃허브 프로필 정보에 일치하는 관리자 정보가 존재할 경우
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 깃허브_토큰_발급() {
                // given
                Admin 관리자 = new Admin(AdminGithubFixture.황병국.email, "010-1234-5678", AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

                when(githubClient.requestGithubToken(AdminGithubFixture.황병국.code)).thenReturn(AdminGithubFixture.황병국.token);
                when(githubClient.requestGithubProfile(AdminGithubFixture.황병국.token)).thenReturn(AdminGithubProfileResponse.of(AdminGithubFixture.황병국.email));
                when(adminRepository.findByEmail(AdminGithubFixture.황병국.email)).thenReturn(Optional.of(관리자));
                when(jwtTokenProvider.createToken(관리자.getEmail())).thenReturn("valid access token");

                // when
                AdminTokenResponse 깃허브_토큰_정보 = adminAuthService.createTokenByGithub(AdminGithubFixture.황병국.code);

                // then
                assertThat(깃허브_토큰_정보.getAccessToken()).isNotNull();
            }
        }

        @Nested
        class 실패 {

            /**
             * Given 깃허브로 회원가입된 관리자를 생성한다.
             * When  깃허브를 통해 발급된 코드(Code)로 깃허브 토큰 발급을 요청한다.
             * And   깃허브 토큰을 통해 깃허브 프로필 정보를 요청한다.
             * And   깃허브 프로필 정보에 일치하는 관리자 정보가 존재하지 않을 경우
             * Then  토큰을 발급할 수 없다.
             */
            @Test
            void 존재하지_않는_관리자_토큰_발급() {
                // given
                Admin 관리자 = new Admin(AdminGithubFixture.황병국.email, "010-1234-5678", AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

                when(githubClient.requestGithubToken(AdminGithubFixture.황병국.code)).thenReturn(AdminGithubFixture.황병국.token);
                when(githubClient.requestGithubProfile(AdminGithubFixture.황병국.token)).thenReturn(AdminGithubProfileResponse.of(AdminGithubFixture.황병국.email));
                when(adminRepository.findByEmail(AdminGithubFixture.황병국.email)).thenReturn(Optional.empty());

                // when
                assertThatExceptionOfType(NotFoundAdminException.class)
                        .isThrownBy(() -> {
                            adminAuthService.createTokenByGithub(AdminGithubFixture.황병국.code);
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_ADMIN.getMessage());
            }

            /**
             * Given 깃허브로 회원가입된 관리자를 생성한다.
             * When  깃허브를 통해 발급된 코드(Code)로 깃허브 토큰 발급을 요청한다.
             * And   깃허브 토큰을 통해 깃허브 프로필 정보를 요청한다.
             * And   깃허브 프로필 정보에 일치하는 관리자가 다른 채널로 가입한 관리자일 경우
             * Then  토큰을 발급할 수 없다.
             */
            @Test
            void 다른_채널로_가입한_관리자_토큰_발급() {
                // given
                Admin 관리자 = new Admin(AdminGithubFixture.황병국.email, "010-1234-5678", AdminRole.ADMIN, AdminSignupChannel.SYSTEM, AdminStatus.ACTIVE);

                when(githubClient.requestGithubToken(AdminGithubFixture.황병국.code)).thenReturn(AdminGithubFixture.황병국.token);
                when(githubClient.requestGithubProfile(AdminGithubFixture.황병국.token)).thenReturn(AdminGithubProfileResponse.of(AdminGithubFixture.황병국.email));
                when(adminRepository.findByEmail(AdminGithubFixture.황병국.email)).thenReturn(Optional.of(관리자));

                // when
                assertThatExceptionOfType(NonMatchingSignupChannelException.class)
                        .isThrownBy(() -> {
                            adminAuthService.createTokenByGithub(AdminGithubFixture.황병국.code);
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_SIGNUP_CHANNEL.getMessage());
            }
        }
    }
}
