package com.shop.core.storeManager.step;

import com.shop.core.storeManager.fixture.StoreManagerFixture;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static org.assertj.core.api.Assertions.assertThat;

public class StoreManagerSteps {

    public static void 성공하는_상점_관리자_생성_요청(String 이메일, String 비밀번호, String 핸드폰_번호) {
        ExtractableResponse<Response> 상점_관리자_생성_요청 = 상점_관리자_생성_요청(이메일, 비밀번호, 핸드폰_번호);
        상점_관리자_생성_확인(상점_관리자_생성_요청);
    }

    public static ExtractableResponse<Response> 상점_관리자_생성_요청(String 이메일, String 비밀번호, String 핸드폰_번호) {
        StoreManagerRequest 상점_관리자_생성_요청_정보 = new StoreManagerRequest(이메일, 비밀번호, 핸드폰_번호);
        return post_요청_토큰_미포함("/store-manager", 상점_관리자_생성_요청_정보);
    }

    public static ExtractableResponse<Response> 상점_관리자_생성_요청(StoreManagerFixture storeManagerFixture) {
        return 상점_관리자_생성_요청(storeManagerFixture.이메일, storeManagerFixture.비밀번호, storeManagerFixture.핸드폰_번호);
    }

    public static void 상점_관리자_생성_확인(ExtractableResponse<Response> 상점_관리자_생성_응답) {
        assertThat(상점_관리자_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상점_관리자_생성_실패_확인(ExtractableResponse<Response> 상점_관리자_생성_응답) {
        assertThat(상점_관리자_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}