package com.shop.core.notification.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.shop.common.util.RestAssuredTemplate.get_요청_토큰_미포함;
import static com.shop.common.util.RestAssuredTemplate.get_요청_토큰_포함;
import static org.assertj.core.api.Assertions.assertThat;

public class NotificationSteps {

    public static void 알림_확인_요청_토큰_포함(Long 발송된_알림_번호, String 토큰) {
        get_요청_토큰_포함(토큰, "/notifications/{notificationId}/receive", 발송된_알림_번호, HttpStatus.OK);
    }

    public static void 알림_확인_요청_토큰_미포함(Long 발송된_알림_번호) {
        get_요청_토큰_미포함("/notifications/{notificationId}/receive", 발송된_알림_번호, HttpStatus.UNAUTHORIZED);
    }

    private static ExtractableResponse<Response> 알림_조회_요청(Long 발송된_알림_번호, String 토큰) {
        return get_요청_토큰_포함(토큰, "/notifications/{notificationId}", 발송된_알림_번호, HttpStatus.OK);
    }

    private static ExtractableResponse<Response> 모든_발송된_알림_조회_요청(String 토큰) {
        return get_요청_토큰_포함(토큰, "/notifications", HttpStatus.OK);
    }

    public static void 알림_읽음_확인(Long 발송된_알림_번호, String 토큰) {
        ExtractableResponse<Response> 알림_조회_요청_응답 = 알림_조회_요청(발송된_알림_번호, 토큰);
        assertThat(알림_조회_요청_응답.jsonPath().getString("notification_status")).isEqualTo("READ");
    }

    public static void 알림_읽지_않음_확인(Long 발송된_알림_번호, String 토큰) {
        ExtractableResponse<Response> 알림_조회_요청_응답 = 알림_조회_요청(발송된_알림_번호, 토큰);
        assertThat(알림_조회_요청_응답.jsonPath().getString("notification_status")).isEqualTo("UNREAD");
    }

    public static void 알림_발송_확인(String 토큰, List<String> 확인할_회원_이메일_목록) {
        ExtractableResponse<Response> 발송된_모든_알림_조회_응답 = 모든_발송된_알림_조회_요청(토큰);

        List<String> memberEmails = 발송된_모든_알림_조회_응답.jsonPath().getList("member_email");
        assertThat(memberEmails).containsAnyElementsOf(확인할_회원_이메일_목록);
    }

    public static void 알림_발송_없음_확인(String 토큰) {
        ExtractableResponse<Response> 발송된_모든_알림_조회_응답 = 모든_발송된_알림_조회_요청(토큰);

        List<String> memberEmails = 발송된_모든_알림_조회_응답.jsonPath().getList("member_email");
        assertThat(memberEmails).isEmpty();
    }
}
