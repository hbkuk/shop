package com.shop.core.point.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.member.exception.NotFoundMemberException;
import com.shop.core.point.application.dto.PaymentInfoResponse;
import com.shop.core.point.domain.*;
import com.shop.core.point.presentation.dto.PaymentWebhookRequest;
import com.shop.core.point.presentation.dto.PointResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.shop.core.member.fixture.MemberFixture.스미스;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("포인트 서비스 레이어 테스트")
@ExtendWith(MockitoExtension.class)
public class PointServiceMockTest {

    PointService pointService;

    @Mock
    MemberService memberService;

    @Mock
    WebhookClient webhookClient;

    @Mock
    PointRepository pointRepository;

    @BeforeEach
    void 사전_포인트_서비스_생성() {
        pointService = new PointService(pointRepository, webhookClient, memberService);
    }

    @Nested
    class 포인트_충전 {

        @Nested
        class 성공 {

            /**
             * Given  회원을 생성한다.
             * When   결제 번호에 해당하는 결제 정보를 요청한다.
             * Then   결제 정보에 일치하는 회원을 찾은 후 포인트를 충전한다.
             */
            @Test
            void 포인트_충전() {
                // given
                Member 회원_정보 = new Member(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                when(memberService.findMemberByEmail(스미스.이메일)).thenReturn(회원_정보);

                String 결제_번호 = "1234567890";
                int 금액 = 30000;
                when(webhookClient.requestPaymentInfo(결제_번호)).thenReturn(PaymentInfoResponse.of(결제_번호, 스미스.이메일, PaymentStatus.PAID, 금액));

                Point 포인트 = new Point(금액, PointStatus.ACTIVE, PointType.CHARGE, 스미스.이메일);
                ReflectionTestUtils.setField(포인트, "id", 1L);
                when(pointRepository.save(any(Point.class))).thenReturn(포인트);

                // when
                PaymentWebhookRequest 결재_웹훅_정보 = PaymentWebhookRequest.of("1234567890", PaymentStatus.PAID);
                PointResponse 포인트_충전_정보 = pointService.receivePointWebhook(결재_웹훅_정보);

                // then
                assertThat(포인트_충전_정보.getAmount()).isEqualTo(금액);
            }

        }

        @Nested
        class 실패 {

            /**
             * When   결제 번호에 해당하는 결제 정보를 요청할 때
             * And    결제 번호에 해당하는 결제 정보가 없을 경우
             * Then   포인트가 충전되지 않는다.
             */
            @Test
            void 존재하지_않는_결제_번호() {
                // given
                String 결제_번호 = "1234567890";
                when(webhookClient.requestPaymentInfo(결제_번호)).thenThrow(new IllegalArgumentException(ErrorType.INVALID_PAYMENT_ID.getMessage()));

                // when, then
                PaymentWebhookRequest 결재_웹훅_정보 = PaymentWebhookRequest.of("1234567890", PaymentStatus.PAID);
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            pointService.receivePointWebhook(결재_웹훅_정보);
                        })
                        .withMessageMatching(ErrorType.INVALID_PAYMENT_ID.getMessage());

            }

            /**
             * When   결제 번호에 해당하는 결제 정보를 요청할 때
             * Then   결제 정보에 일치하는 회원을 찾지 못한 경우
             * Then   포인트가 충전되지 않는다.
             */
            @Test
            void 존재하지_않는_회원() {
                // given
                when(memberService.findMemberByEmail(스미스.이메일)).thenThrow(new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));

                String 결제_번호 = "1234567890";
                int 금액 = 30000;
                when(webhookClient.requestPaymentInfo(결제_번호)).thenReturn(PaymentInfoResponse.of(결제_번호, 스미스.이메일, PaymentStatus.PAID, 금액));

                // when, then
                PaymentWebhookRequest 결재_웹훅_정보 = PaymentWebhookRequest.of("1234567890", PaymentStatus.PAID);
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            pointService.receivePointWebhook(결재_웹훅_정보);
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_MEMBER.getMessage());
            }
        }
    }
}
