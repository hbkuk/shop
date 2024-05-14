package com.shop.core.product;

import com.shop.common.util.UserAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends UserAcceptanceTest {

    @Nested
    class 상품_등록 {


        @Nested
        class 성공 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * When  토큰을 통해 상품을 등록한다.
             * Then  정상적으로 상품이 등록된다.
             */
            @Test
            void 토큰_포함_상품_등록() {
                // given
                //String 상점_관리자_토큰 =

                // when

                // then
            }

        }

        @Nested
        class 실패 {

            /**
             * Given 상점 관리자를 생성한다.
             * When  토큰 없이 상품을 등록할 경우
             * Then  상품이 등록되지 않는다.
             */
            // TODO 토큰 없이 상품 등록
        }

    }

    @Nested
    class 상품_수정 {

        @Nested
        class 성공 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 상품을 등록한다.
             * When  토큰을 통해 등록된 상품을 수정한다.
             * Then  정상적으로 상품의 상품이 수정된다.
             */

        }

        @Nested
        class 실패 {

            /**
             * Given 상점 관리자를 생성하고, 토큰을 발급한다.
             * And   토큰을 통해 상품을 등록한다.
             * When  토큰 없이 등록된 상품을 수정한다.
             * Then  상품이 수정되지 않는다.
             */

            // TODO: 이름, 설명 수정
            // TODO: 가격 수정
            // TODO: 판매 수량 수정
        }
    }
}
