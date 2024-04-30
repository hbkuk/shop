package com.shop.core.notification.acceptance;

import com.shop.common.util.UserAcceptanceTest;
import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationRepository;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static com.shop.core.admin.auth.fixture.AdminGithubFixture.황병국;
import static com.shop.core.auth.step.AuthSteps.회원생성_후_토큰_발급;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("알림 관련 인수 테스트")
public class NotificationAcceptanceTest extends UserAcceptanceTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Nested
    class 알림_확인 {

        String 관리자_토큰;
        String 회원_토큰;

        Notification 발송된_알림_정보;

        @BeforeEach
        void 사전_준비() {
            관리자_토큰 = 관리자_생성_후_토큰_발급(황병국);
            회원_토큰 = 회원생성_후_토큰_발급(스미스);

            Notification 발송할_알림_정보 = new Notification(NotificationType.EVENT_NOTIFICATION, "새로운 쿠폰 발급 이벤트가 추가되었습니다.", LocalDateTime.now(), NotificationStatus.UNREAD, 스미스.이메일, 황병국.email);
            발송된_알림_정보 = notificationRepository.save(발송할_알림_정보);
        }

        @Nested
        class 성공 {

            /**
             * Given 알림을 발송한다.
             * When  알림을 확인한다.
             * Then  알림이 확인한 상태로 변경된다.
             */
            @Test
            void 알림_확인_성공() {
                // when
                given().log().all()
                        .header("Authorization", 회원_토큰)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/notifications/{notificationId}/receive", 발송된_알림_정보.getId())
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                // then
                ExtractableResponse<Response> 알림_조회_요청_응답 = given().log().all()
                        .header("Authorization", 회원_토큰)
                        .when()
                        .get("/notifications/{notificationId}", 발송된_알림_정보.getId())
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(알림_조회_요청_응답.jsonPath().getString("notification_status")).isEqualTo("READ");
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
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/notifications/{notificationId}/receive", 발송된_알림_정보.getId())
                        .then().log().all()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .extract();

                // then
                ExtractableResponse<Response> 알림_조회_요청_응답 = given().log().all()
                        .header("Authorization", 회원_토큰)
                        .when()
                        .get("/notifications/{notificationId}", 발송된_알림_정보.getId())
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                assertThat(알림_조회_요청_응답.jsonPath().getString("notification_status")).isEqualTo("UNREAD");
            }
        }
    }
}
