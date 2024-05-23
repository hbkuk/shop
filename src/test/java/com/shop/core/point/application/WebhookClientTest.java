package com.shop.core.point.application;

import com.shop.common.annotation.ComponentTest;
import com.shop.common.exception.ErrorType;
import com.shop.core.point.application.dto.PaymentInfoResponse;
import org.hibernate.annotations.DiscriminatorFormula;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ComponentTest
@DiscriminatorFormula("웹훅 클라이언트 컴포넌트 테스트")
public class WebhookClientTest {

    @Autowired
    WebhookClient webhookClient;

    @Nested
    class 결제_정보_요청 {

        @Nested
        class 성공 {

            /**
             * Given  결제를 진행한다.
             * When   결제 번호에 해당하는 결제 정보를 요청한다.
             * Then   결제 정보를 확인할 수 있다.
             */
            @Test
            void 결제_정보_요청() {
                // given
                String 결제_번호 = "1234567890";

                // when
                PaymentInfoResponse 결제_정보_응답 = webhookClient.requestPaymentInfo(결제_번호);

                // then
                assertThat(결제_정보_응답).isNotNull();
            }
        }

        @Nested
        class 실패 {

            /**
             * When  결제 번호에 해당하는 결제 정보를 요청할 때
             * And   결제 번호에 해당하는 결제 정보가 없을 경우
             * Then  결제 정보를 확인할 수 없다.
             */
            @Test
            void 결제_정보_요청() {
                // given
                String 존재하지_않는_결제_번호 = "9999999999";

                // when, then
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            webhookClient.requestPaymentInfo(존재하지_않는_결제_번호);
                        })
                        .withMessageMatching(ErrorType.INVALID_PAYMENT_ID.getMessage());
            }
        }
    }
}
