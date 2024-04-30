package com.shop.core.notification.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationSteps {

    public static void 알림_확인_요청_토큰_포함(Long 발송된_알림_번호, String 토큰) {
        given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/notifications/{notificationId}/receive", 발송된_알림_번호)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 알림_확인_요청_토큰_미포함(Long 발송된_알림_번호) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/notifications/{notificationId}/receive", 발송된_알림_번호)
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    private static ExtractableResponse<Response> 알림_조회_요청(Long 발송된_알림_번호, String 토큰) {
        return given().log().all()
                .header("Authorization", 토큰)
                .when()
                .get("/notifications/{notificationId}", 발송된_알림_번호)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 알림_읽음_확인(Long 발송된_알림_번호, String 토큰) {
        ExtractableResponse<Response> 알림_조회_요청_응답 = 알림_조회_요청(발송된_알림_번호, 토큰);
        assertThat(알림_조회_요청_응답.jsonPath().getString("notification_status")).isEqualTo("READ");
    }

    public static void 알림_읽지_않음_확인(Long 발송된_알림_번호, String 토큰) {
        ExtractableResponse<Response> 알림_조회_요청_응답 = 알림_조회_요청(발송된_알림_번호, 토큰);
        assertThat(알림_조회_요청_응답.jsonPath().getString("notification_status")).isEqualTo("UNREAD");
    }
}
