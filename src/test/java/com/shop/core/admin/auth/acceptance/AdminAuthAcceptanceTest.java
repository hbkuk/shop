package com.shop.core.admin.auth.acceptance;

import com.shop.common.util.AcceptanceTest;
import com.shop.core.admin.auth.domain.*;
import com.shop.core.admin.auth.fixture.AdminGithubFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("관리자 인증 인수 테스트")
public class AdminAuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    AdminRepository adminRepository;

    @Nested
    class 관리자_깃허브_토큰_발급 {

        @Nested
        class 성공 {

            /**
             * Given 관리자가 등록된다.
             * When  관리자가 깃허브를 통해 토큰을 발급한다.
             * Then  정상적으로 토큰이 발급된다.
             */
            @Test
            void 깃허브로_관리자_토큰_발급() {
                // given
                Admin 관리자 = new Admin(AdminGithubFixture.황병국.email, "010-1234-5678", AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);
                adminRepository.save(관리자);

                // when
                Map<String, String> githubCodeParams = new HashMap<>();
                githubCodeParams.put("code", "github_admin_code_001");


                ExtractableResponse<Response> 로그인_요청_응답 = RestAssured
                        .given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(githubCodeParams)
                        .when()
                        .post("/admin/login/github")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value()).extract();

                String 토큰 = 로그인_요청_응답.jsonPath().getString("accessToken");

                // then
                assertThat(토큰).isNotBlank();
            }
        }
    }
}
