package com.shop.core.store.application;

import com.shop.common.domain.auth.LoginUser;
import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.store.domain.Store;
import com.shop.core.store.domain.StoreRepository;
import com.shop.core.store.domain.StoreStatus;
import com.shop.core.store.exception.DuplicateStoreCreationException;
import com.shop.core.store.exception.DuplicateStoreNameException;
import com.shop.core.store.exception.NonMatchingStoreManagerException;
import com.shop.core.store.exception.NotFoundStoreException;
import com.shop.core.store.presentation.dto.StoreRequest;
import com.shop.core.store.presentation.dto.StoreResponse;
import com.shop.core.store.presentation.dto.StoreStatusRequest;
import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManager.exception.NotFoundStoreManagerException;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.storeManager.fixture.StoreManagerFixture.김상점;
import static com.shop.core.storeManager.fixture.StoreManagerFixture.이상점;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("상점 서비스 레이어 테스트")
public class StoreServiceTest extends ApplicationTest {

    @Autowired
    StoreService storeService;

    @Autowired
    StoreManagerService storeManagerService;

    @Autowired
    StoreRepository storeRepository;

    @Nested
    class 상점_등록 {

        @Nested
        class 성공 {

            @Test
            void 상점_등록() {
                // given
                StoreManagerRequest 상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_등록_정보);

                StoreRequest 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");

                // when
                storeService.createStore(상점_등록_정보, LoginUser.of(김상점.이메일));

                // then
                Store 찾은_상점_정보 = storeRepository.findByStoreManagerEmail(김상점.이메일).get();
                assertThat(찾은_상점_정보.getName()).isEqualTo(상점_등록_정보.getName());
            }

        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_상점_관리자_정보로_상점_등록() {
                // given
                StoreRequest 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");

                // when, then
                assertThatExceptionOfType(NotFoundStoreManagerException.class)
                        .isThrownBy(() -> {
                            storeService.createStore(상점_등록_정보, LoginUser.of(김상점.이메일));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_STORE_MANAGER.getMessage());
            }

            @Test
            void 이미_존재하는_상점명으로_상점_등록() {
                // given
                StoreManagerRequest 첫번째_상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(첫번째_상점_관리자_등록_정보);

                StoreManagerRequest 두번째_상점_관리자_등록_정보 = StoreManagerRequest.of(이상점.이메일, 이상점.비밀번호, 이상점.핸드폰_번호);
                storeManagerService.createStoreManager(두번째_상점_관리자_등록_정보);

                StoreRequest 첫번째_상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                storeService.createStore(첫번째_상점_등록_정보, LoginUser.of(김상점.이메일));

                // when, then
                StoreRequest 두번째_상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 건강과 지속 가능성을 증진시킵니다.");
                assertThatExceptionOfType(DuplicateStoreNameException.class)
                        .isThrownBy(() -> {
                            storeService.createStore(두번째_상점_등록_정보, LoginUser.of(이상점.이메일));
                        })
                        .withMessageMatching(ErrorType.DUPLICATE_STORE_NAME.getMessage());
            }

            @Test
            void 관리자가_이미_등록한_상점이_있는_상태에서_상점_등록() {
                // given
                StoreManagerRequest 상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_등록_정보);

                StoreRequest 첫번째_상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                storeService.createStore(첫번째_상점_등록_정보, LoginUser.of(김상점.이메일));

                // when, then
                StoreRequest 두번째_상점_등록_정보 = StoreRequest.of("건강만 하자", "당신의 건강을 증진시킵니다.");
                assertThatExceptionOfType(DuplicateStoreCreationException.class)
                        .isThrownBy(() -> {
                            storeService.createStore(두번째_상점_등록_정보, LoginUser.of(김상점.이메일));
                        })
                        .withMessageMatching(ErrorType.DUPLICATE_STORE_CREATION.getMessage());
            }

        }
    }

    @Nested
    class 상점_상태_변경 {

        @Nested
        class 성공 {

            @Test
            void 상점_상태_변경() {
                // given
                StoreManagerRequest 상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_등록_정보);

                StoreRequest 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                StoreResponse 등록된_상점_등록_정보 = storeService.createStore(상점_등록_정보, LoginUser.of(김상점.이메일));

                // when
                storeService.updateStatus(StoreStatusRequest.of(등록된_상점_등록_정보.getId(), StoreStatus.CLOSED), LoginUser.of(김상점.이메일));

                // then
                Store 찾은_상점_정보 = storeRepository.findById(등록된_상점_등록_정보.getId()).get();
                assertThat(찾은_상점_정보.getStatus()).isEqualTo(StoreStatus.CLOSED);
            }

        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_상점_관리가_상점_상태_변경() {
                // given
                StoreManagerRequest 상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_등록_정보);

                StoreRequest 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                StoreResponse 등록된_상점_등록_정보 = storeService.createStore(상점_등록_정보, LoginUser.of(김상점.이메일));

                // when, then
                assertThatExceptionOfType(NotFoundStoreManagerException.class)
                        .isThrownBy(() -> {
                            storeService.updateStatus(StoreStatusRequest.of(등록된_상점_등록_정보.getId(), StoreStatus.CLOSED), LoginUser.of(이상점.이메일));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_STORE_MANAGER.getMessage());

                Store 찾은_상점_정보 = storeRepository.findById(등록된_상점_등록_정보.getId()).get();
                assertThat(찾은_상점_정보.getStatus()).isEqualTo(StoreStatus.OPEN);
            }

            @Test
            void 존재하지_않는_상점의_상태_변경() {
                // given
                StoreManagerRequest 상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(상점_관리자_등록_정보);

                Long 존재하지_않는_상점_번호 = 999L;

                // when, then
                assertThatExceptionOfType(NotFoundStoreException.class)
                        .isThrownBy(() -> {
                            storeService.updateStatus(StoreStatusRequest.of(존재하지_않는_상점_번호, StoreStatus.CLOSED), LoginUser.of(김상점.이메일));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_STORE.getMessage());
            }

            @Test
            void 다른_상점_관리자의_상점_상태_변경() {
                // given
                StoreManagerRequest 첫번째_상점_관리자_등록_정보 = StoreManagerRequest.of(김상점.이메일, 김상점.비밀번호, 김상점.핸드폰_번호);
                storeManagerService.createStoreManager(첫번째_상점_관리자_등록_정보);

                StoreManagerRequest 두번째_상점_관리자_등록_정보 = StoreManagerRequest.of(이상점.이메일, 이상점.비밀번호, 이상점.핸드폰_번호);
                storeManagerService.createStoreManager(두번째_상점_관리자_등록_정보);

                StoreRequest 상점_등록_정보 = StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다.");
                StoreResponse 등록된_상점_등록_정보 = storeService.createStore(상점_등록_정보, LoginUser.of(김상점.이메일));

                // when, then
                assertThatExceptionOfType(NonMatchingStoreManagerException.class)
                        .isThrownBy(() -> {
                            storeService.updateStatus(StoreStatusRequest.of(등록된_상점_등록_정보.getId(), StoreStatus.CLOSED), LoginUser.of(이상점.이메일));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_STORE_MANAGER.getMessage());
            }
        }
    }
}
