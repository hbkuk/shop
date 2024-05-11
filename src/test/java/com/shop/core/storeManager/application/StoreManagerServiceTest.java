package com.shop.core.storeManager.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.storeManager.domain.StoreManager;
import com.shop.core.storeManager.domain.StoreManagerRepository;
import com.shop.core.storeManager.domain.StoreManagerStatus;
import com.shop.core.storeManager.exception.DuplicateEmailException;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("상점 관리자 서비스 레이어 테스트")
public class StoreManagerServiceTest extends ApplicationTest {

    @Autowired
    StoreManagerService storeManagerService;

    @Autowired
    StoreManagerRepository storeManagerRepository;

    @Nested
    class 상점_관리자_생성 {

        @Nested
        class 성공 {

            @Test
            @DisplayName("상점 관리자를 생성한다.")
            void 상점_관리자_생성() {
                // given
                StoreManagerRequest 상점_관리_생성_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);

                // when
                storeManagerService.createStoreManager(상점_관리_생성_정보);

                // then
                assertThat(storeManagerRepository.findByEmail(상점_관리_생성_정보.getEmail()).get().getEmail())
                        .isEqualTo(상점_관리_생성_정보.getEmail());

            }
        }

        @Nested
        class 실패 {

            @BeforeEach
            void 사전_상점_관리자_생성() {
                StoreManager 상점_관리자 = new StoreManager(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호, StoreManagerStatus.ACTIVE);
                storeManagerRepository.save(상점_관리자);
            }

            @Test
            @DisplayName("동일한 이메일의 상점 관리자를 생성할 경우 생성할 수 없다.")
            void 동일한_상점_관리자_이메일로_생성() {
                // given
                StoreManagerRequest 상점_관리자_생성_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);

                // when, then
                assertThatExceptionOfType(DuplicateEmailException.class)
                        .isThrownBy(() -> {
                            storeManagerService.createStoreManager(상점_관리자_생성_정보);
                        })
                        .withMessageMatching(ErrorType.DUPLICATE_MEMBER_EMAIL.getMessage());
            }

        }
    }

    @Nested
    class 상점_관리자_조회 {

        @Nested
        class 성공 {

        }

        @Nested
        class 실패 {

        }
    }


}
