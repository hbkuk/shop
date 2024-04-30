package com.shop.core.notification.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.admin.auth.domain.Admin;
import com.shop.core.admin.auth.domain.AdminRole;
import com.shop.core.admin.auth.domain.AdminSignupChannel;
import com.shop.core.admin.auth.domain.AdminStatus;
import com.shop.core.admin.auth.exception.NotFoundAdminException;
import com.shop.core.auth.domain.LoginUser;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.member.exception.NonMatchingMemberException;
import com.shop.core.member.exception.NotFoundMemberException;
import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationRepository;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import com.shop.core.notification.exception.CannotNotificationReadException;
import com.shop.core.notification.exception.NotFoundNotificationException;
import com.shop.core.notification.presentation.dto.NotificationRequest;
import com.shop.core.notification.presentation.dto.NotificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.shop.core.admin.auth.fixture.AdminGithubFixture.황병국;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.member.fixture.MemberFixture.존슨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("알림 서비스 레이어 테스트")
public class NotificationServiceTest extends ApplicationTest {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationRepository notificationRepository;

    @Nested
    class 알림_발송 {

        Member 생성된_회원;
        Admin 생성된_관리자;

        String 존재하지_않는_회원_이메일 = "non-existent-member001@email.com";
        String 존재하지_않는_관리자_이메일 = "non-existent-admin001@email.com";

        @BeforeEach
        void 사전_준비() {
            생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

        }

        @Nested
        class 성공 {

            @Test
            @DisplayName("정상적으로 알림이 발송된다.")
            void 알림_발송_성공() {
                // given
                NotificationRequest 발송할_알림_정보 = NotificationRequest.of(NotificationType.EVENT_NOTIFICATION, "신규 이벤트가 진행 중입니다.", 생성된_회원.getEmail());

                // when
                NotificationResponse response = notificationService.send(발송할_알림_정보, LoginUser.of(생성된_관리자.getEmail()));

                // then
                Notification 찾은_알림_정보 = notificationRepository.findById(response.getId()).get();
                assertThat(찾은_알림_정보.getNotificationStatus()).isEqualTo(NotificationStatus.UNREAD);
            }
        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_회원_알림_발송_실패() {
                // given
                NotificationRequest 발송할_알림_정보 = NotificationRequest.of(NotificationType.EVENT_NOTIFICATION, "신규 이벤트가 진행 중입니다.", 존재하지_않는_회원_이메일);

                // when, then
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            notificationService.send(발송할_알림_정보, LoginUser.of(생성된_관리자.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_MEMBER.getMessage());
            }

            @Test
            void 존재하지_않는_관리자_알림_발송_실패() {
                // given
                NotificationRequest 발송할_알림_정보 = NotificationRequest.of(NotificationType.EVENT_NOTIFICATION, "신규 이벤트가 진행 중입니다.", 생성된_회원.getEmail());

                // when, then
                assertThatExceptionOfType(NotFoundAdminException.class)
                        .isThrownBy(() -> {
                            notificationService.send(발송할_알림_정보, LoginUser.of(존재하지_않는_관리자_이메일));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_ADMIN.getMessage());
            }
        }
    }

    @Nested
    class 알림_확인 {

        Member 첫번째_생성된_회원;
        Member 두번째_생성된_회원;
        Admin 생성된_관리자;

        String 존재하지_않는_회원_이메일 = "non-existent-member001@email.com";

        Notification 발송된_알림;


        @BeforeEach
        void 사전_준비() {
            첫번째_생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            두번째_생성된_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

            NotificationRequest 발송할_알림_정보 = NotificationRequest.of(NotificationType.EVENT_NOTIFICATION, "신규 이벤트가 진행 중입니다.", 첫번째_생성된_회원.getEmail());
            발송된_알림 = notificationRepository.save(발송할_알림_정보.toEntity(LocalDateTime.now(), 생성된_관리자.getEmail()));
        }


        @Nested
        class 성공 {

            @Test
            void 알림_확인_성공() {
                // when
                notificationService.read(발송된_알림.getId(), LoginUser.of(첫번째_생성된_회원.getEmail()));

                // then
                Notification 찾은_알림 = notificationRepository.findById(발송된_알림.getId()).get();
                assertThat(찾은_알림.getNotificationStatus()).isEqualTo(NotificationStatus.READ);
            }

        }

        @Nested
        class 실패 {

            @Test
            void 존재하지_않는_알림_확인_실패() {
                // given
                Long 존재하지_않는_알림_번호 = 100L;

                // when, then
                assertThatExceptionOfType(NotFoundNotificationException.class)
                        .isThrownBy(() -> {
                            notificationService.read(존재하지_않는_알림_번호, LoginUser.of(첫번째_생성된_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_NOTIFICATION.getMessage());
            }

            @Test
            void 타_회원의_알림_확인_실패() {
                // when, then
                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            notificationService.read(발송된_알림.getId(), LoginUser.of(두번째_생성된_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_MEMBER.getMessage());
            }

            @Test
            void 읽을_수_없는_알림_확인_실패() {
                // given
                Notification 발송_실패한_알림
                        = new Notification(NotificationType.EVENT_NOTIFICATION, "신규 이벤트가 진행 중입니다.", LocalDateTime.now(), NotificationStatus.FAILED, 첫번째_생성된_회원.getEmail(), 생성된_관리자.getEmail());
                notificationRepository.save(발송_실패한_알림);

                // when, then
                assertThatExceptionOfType(CannotNotificationReadException.class)
                        .isThrownBy(() -> {
                            notificationService.read(발송_실패한_알림.getId(), LoginUser.of(첫번째_생성된_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.CANNOT_NOTIFICATION_READ.getMessage());
            }
        }

    }

    @Nested
    class 알림_찾기 {

    }
}
