package com.shop.core.storeManagerAuth.step;

import com.shop.core.storeManager.fixture.StoreManagerFixture;
import com.shop.core.storeManagerAuth.presentation.dto.StoreManagerAuthRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static org.assertj.core.api.Assertions.assertThat;

public class StoreManagerSteps {

    public static void 토큰_확인(String 발급된_토큰) {
        assertThat(발급된_토큰).isNotBlank();
    }

    public static String 성공하는_상점_관리자_토큰_발급_요청(StoreManagerFixture 상점_관리자) {
        StoreManagerAuthRequest storeManagerAuthRequest = StoreManagerAuthRequest.of(상점_관리자.이메일, 상점_관리자.비밀번호);
        ExtractableResponse<Response> 응답 = post_요청_토큰_미포함("/login/store-manager", storeManagerAuthRequest, HttpStatus.OK);

        return 응답.jsonPath().getString("access_token");
    }

    public static void 실패하는_상품_관리자_토큰_발급_요청(String 이메일, String 비밀번호) {
        StoreManagerAuthRequest storeManagerAuthRequest = StoreManagerAuthRequest.of(이메일, 비밀번호);
        post_요청_토큰_미포함("/login/store-manager", storeManagerAuthRequest, HttpStatus.BAD_REQUEST);
    }
}
