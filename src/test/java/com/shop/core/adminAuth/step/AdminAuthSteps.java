package com.shop.core.adminAuth.step;

import com.shop.core.adminAuth.presentation.dto.AdminGithubCodeRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static org.assertj.core.api.Assertions.assertThat;

public class AdminAuthSteps {

    public static ExtractableResponse<Response> 깃허브_토큰_발급_요청(AdminGithubCodeRequest 깃허브_코드_정보) {
        return post_요청_토큰_미포함("/admin/login/github", 깃허브_코드_정보, HttpStatus.OK);
    }

    public static void 관리자_토큰_발급_확인(ExtractableResponse<Response> 로그인_요청_응답) {
        String 토큰 = 로그인_요청_응답.jsonPath().getString("access_token");

        assertThat(토큰).isNotBlank();
    }
}
