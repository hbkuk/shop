package com.shop.core.notification.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationRepository;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.shop.core.admin.auth.fixture.AdminGithubFixture.황병국;
import static com.shop.core.auth.step.AuthSteps.회원생성_후_토큰_발급;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.notification.step.NotificationSteps.*;

@DisplayName("알림 관련 인수 테스트")
public class NotificationAcceptanceTest extends UserAcceptanceTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Nested
    class 알림_읽음 {

        String 관리자_토큰;
        String 회원_토큰;

        Notification 발송된_알림_정보;

        @BeforeEach
        void 사전_준비() {
            관리자_토큰 = 관리자_생성_후_토큰_발급(황병국);
            회원_토큰 = 회원생성_후_토큰_발급(스미스);

            Notification 발송할_알림_정보
                    = new Notification(NotificationType.EVENT_NOTIFICATION, "새로운 쿠폰 발급 이벤트가 추가되었습니다.", LocalDateTime.now(), NotificationStatus.UNREAD, 스미스.이메일, 황병국.email);
            발송된_알림_정보 = notificationRepository.save(발송할_알림_정보);
        }

        @Nested
        class 성공 {

            /**
             * Given 알림을 발송한다.
             * When  알림을 읽는다.
             * Then  알림이 읽은 상태로 변경된다.
             */
            @Test
            void 알림_읽음_성공() {
                // when
                알림_확인_요청_토큰_포함(발송된_알림_정보.getId(), 회원_토큰);

                // then
                알림_읽음_확인(발송된_알림_정보.getId(), 회원_토큰);
            }

        }

        @Nested
        class 실패 {

            /**
             * Given 알림을 특정 회원에게 발송한다.
             * When  회원 정보 없이 알림을 확인할 경우
             * Then  알림을 확인할 수 없다.
             */
            @Test
            void 회원_정보_없이_알림_확인_실패() {
                // given
                알림_확인_요청_토큰_미포함(발송된_알림_정보.getId());

                // then
                알림_읽지_않음_확인(발송된_알림_정보.getId(), 회원_토큰);
            }
        }
    }
}
