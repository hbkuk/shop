package com.shop.core.storeManagerSecurity.application;

import com.shop.common.security.PasswordSecurityManager;
import com.shop.common.util.ApplicationTest;
import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManager.domain.StoreManager;
import com.shop.core.storeManager.domain.StoreManagerRepository;
import com.shop.core.storeManager.domain.StoreManagerStatus;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import com.shop.core.storeManagerSecurity.domain.StoreManagerSecurity;
import com.shop.core.storeManagerSecurity.domain.StoreManagerSecurityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상점 관리자 보안 서비스 레이어 테스트 ")
public class StoreManagerSecurityTest extends ApplicationTest {

    @Autowired
    StoreManagerSecurityService storeManagerSecurityService;

    @Autowired
    StoreManagerSecurityRepository storeManagerSecurityRepository;

    @Autowired
    PasswordSecurityManager passwordSecurityManager;

    @Autowired
    StoreManagerRepository storeManagerRepository;

    @Autowired
    StoreManagerService storeManagerService;

    @Nested
    class 비밀번호_보안_적용 {

        @Nested
        class 성공 {

            @Test
            void 비밀번호_보안_적용() {
                // given
                StoreManager 상점_관리자 = new StoreManager(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호, StoreManagerStatus.ACTIVE);
                StoreManager 저장된_상점_관리_정보 = storeManagerRepository.save(상점_관리자);

                // when
                storeManagerSecurityService.applySecurity(저장된_상점_관리_정보);

                // then
                StoreManagerSecurity 저장된_보안_정보 = storeManagerSecurityRepository.findByStoreManager(저장된_상점_관리_정보);
                assertThat(저장된_보안_정보.getSalt()).isNotBlank();

            }

        }
    }

    @Nested
    class 비밀번호_검증 {

        @Nested
        class 성공 {

            @Test
            void 암호화_전_비밀번호_동일() {
                // given
                StoreManagerRequest 상점_관리자_생성_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_생성_정보);

                // when
                boolean 비밀번호_일치_여부 = storeManagerSecurityService.verifyPassword(김상점.비밀번호, storeManagerRepository.findByEmail(김상점.이메일).get());

                // then
                assertThat(비밀번호_일치_여부).isTrue();
            }

        }

        @Nested
        class 실패 {

            @Test
            void 암호화_전_비밀번호_다름() {
                // given
                StoreManagerRequest 상점_관리자_생성_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_생성_정보);

                // when
                boolean 비밀번호_일치_여부 = storeManagerSecurityService.verifyPassword("random" + 김상점.비밀번호, storeManagerRepository.findByEmail(김상점.이메일).get());

                // then
                assertThat(비밀번호_일치_여부).isFalse();
            }

        }
    }
}
