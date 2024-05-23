package com.shop.core.point.step;

import com.shop.core.point.presentation.dto.PaymentWebhookRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.get_요청_토큰_포함;
import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static com.shop.core.point.fixture.PaymentFixture.첫번째_결제_정보;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointStep {

    public static ExtractableResponse<Response> 웹훅_포인트_충전_요청(PaymentWebhookRequest 결제_정보) {
        return post_요청_토큰_미포함("/payment-webhook/points", 결제_정보, HttpStatus.CREATED);
    }

    public static void 포인트_충전_확인(String 회원_토큰, ExtractableResponse<Response> 포인트_충전_요청_응답) {
        ExtractableResponse<Response> 포인트_조회_요청_응답 = get_요청_토큰_포함(회원_토큰, "/points/{id}", getCreatedLocationId(포인트_충전_요청_응답), HttpStatus.OK);

        assertEquals(포인트_조회_요청_응답.jsonPath().getInt("amount"), 첫번째_결제_정보.금액);
    }
}
