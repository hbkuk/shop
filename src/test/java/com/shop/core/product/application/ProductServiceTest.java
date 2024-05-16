package com.shop.core.product.application;

import com.shop.common.domain.auth.LoginUser;
import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.product.domain.Product;
import com.shop.core.product.domain.ProductRepository;
import com.shop.core.product.domain.ProductStatus;
import com.shop.core.product.exception.DuplicateProductNameException;
import com.shop.core.product.presentation.dto.ProductRequest;
import com.shop.core.store.application.StoreService;
import com.shop.core.store.exception.NotFoundStoreException;
import com.shop.core.store.presentation.dto.StoreRequest;
import com.shop.core.store.presentation.dto.StoreResponse;
import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import com.shop.core.storeManager.presentation.dto.StoreManagerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.storeManager.fixture.StoreManagerFixture.이상점;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("상품 서비스 레이어 테스트")
public class ProductServiceTest extends ApplicationTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StoreService storeService;

    @Autowired
    StoreManagerService storeManagerService;

    @Nested
    class 상품_등록 {

        @Nested
        class 성공 {

            StoreManagerResponse 등록된_상점_관리자_정보;
            StoreResponse 등록된_상점_정보;

            @BeforeEach
            void 사전_준비() {
                등록된_상점_관리자_정보 = storeManagerService.createStoreManager(StoreManagerRequest.of(이상점.이메일, 이상점.비밀번호, 이상점.핸드폰_번호));
                등록된_상점_정보 = storeService.createStore(StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다."), LoginUser.of(이상점.이메일));
            }

            @Test
            void 상품_등록() {
                // given
                ProductRequest 등록할_상품_정보 = ProductRequest.of("블링블링 청바지", 100, "블링블링한 멋진 바지를 입어보세요~", 30000, 등록된_상점_정보.getId());

                // when
                productService.registerProduct(등록할_상품_정보, LoginUser.of(이상점.이메일));

                // then
                Product 찾은_상품_정보 = productRepository.findByStoreId(등록된_상점_정보.getId());
                assertThat(찾은_상품_정보.getProductStatus()).isEqualTo(ProductStatus.AVAILABLE);
            }


        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_상점_번호에_상품_등록() {
                // given
                StoreManagerResponse 등록된_상점_관리자_정보 = storeManagerService.createStoreManager(StoreManagerRequest.of(이상점.이메일, 이상점.비밀번호, 이상점.핸드폰_번호));
                Long 존재하지_않는_상점_번호 = 999L;

                ProductRequest 등록할_상품_정보 = ProductRequest.of("블링블링 청바지", 100, "블링블링한 멋진 바지를 입어보세요~", 30000, 존재하지_않는_상점_번호);

                // when, then
                assertThatExceptionOfType(NotFoundStoreException.class)
                        .isThrownBy(() -> {
                            productService.registerProduct(등록할_상품_정보, LoginUser.of(등록된_상점_관리자_정보.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_STORE.getMessage());
            }

            @Test
            void 이미_등록된_상품명으로_상품_등록() {
                // given
                StoreManagerResponse 등록된_상점_관리자_정보 = storeManagerService.createStoreManager(StoreManagerRequest.of(이상점.이메일, 이상점.비밀번호, 이상점.핸드폰_번호));
                StoreResponse 등록된_상점_정보 = storeService.createStore(StoreRequest.of("패션 팔래트", "당신의 생활을 더 건강하고 지속 가능하게 만들어드립니다."), LoginUser.of(이상점.이메일));

                ProductRequest 등록할_첫번째_상품_정보 = ProductRequest.of("블링블링 청바지", 100, "블링블링한 멋진 바지를 입어보세요~", 30000, 등록된_상점_정보.getId());
                productService.registerProduct(등록할_첫번째_상품_정보, LoginUser.of(이상점.이메일));

                // when, then
                ProductRequest 등록할_두번째_상품_정보 = ProductRequest.of("블링블링 청바지", 50, "블링블링한 멋진 청바지를 입어보세요~", 25000, 등록된_상점_정보.getId());

                assertThatExceptionOfType(DuplicateProductNameException.class)
                        .isThrownBy(() -> {
                            productService.registerProduct(등록할_두번째_상품_정보, LoginUser.of(이상점.이메일));
                        })
                        .withMessageMatching(ErrorType.DUPLICATE_PRODUCT_NAME.getMessage());

            }
        }
    }
}
