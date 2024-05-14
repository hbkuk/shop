package com.shop.core.storeManagerAuth.application;

import com.shop.common.auth.JwtTokenProvider;
import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.member.exception.PasswordMismatchException;
import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManager.exception.NotFoundStoreManagerException;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import com.shop.core.storeManagerAuth.presentation.dto.StoreManagerAuthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("상점 관리자 서비스 레이어 테스트")
public class StoreManagerMemberAuthServiceTest extends ApplicationTest {

    @Autowired
    StoreManagerAuthService storeManagerAuthService;

    @Autowired
    StoreManagerService storeManagerService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Nested
    class 토큰_발급 {

        @Nested
        class 성공 {

            @Test
            void 토큰_발급_요청() {
                // given
                StoreManagerRequest 상점_관리자_생성_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_생성_정보);

                // when
                StoreManagerAuthResponse tokenResponse = storeManagerAuthService.createToken(김상점.이메일, 김상점.비밀번호);

                // then
                assertThat(tokenResponse.getAccessToken()).isNotBlank();
            }

        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_회원_정보로_토큰_발급_요청() {
                // when
                assertThatExceptionOfType(NotFoundStoreManagerException.class)
                        .isThrownBy(() -> {
                            storeManagerAuthService.createToken(김상점.이메일, 김상점.비밀번호);
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_STORE_MANAGER.getMessage());
            }

            @Test
            void 다른_회원_정보로_토큰_발급_요청() {
                // given
                StoreManagerRequest 상점_관리자_생성_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_생성_정보);

                // when
                assertThatExceptionOfType(PasswordMismatchException.class)
                        .isThrownBy(() -> {
                            storeManagerAuthService.createToken(김상점.이메일, "Random" + 김상점.비밀번호);
                        })
                        .withMessageMatching(ErrorType.PASSWORD_MISMATCH.getMessage());
            }

        }

    }
}
