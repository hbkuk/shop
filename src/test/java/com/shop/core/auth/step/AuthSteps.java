package com.shop.core.auth.step;

import com.shop.core.member.fixture.MemberFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {
    public static String 성공하는_토큰_발급_요청(MemberFixture memberFixture) {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberFixture.이메일);
        params.put("password", memberFixture.비밀번호);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    public static void 실패하는_토큰_발급_요청(MemberFixture memberFixture) {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberFixture.이메일);
        params.put("password", memberFixture.비밀번호);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value()).extract();
    }

    public static void 실패하는_토큰_발급_요청(MemberFixture 회원, String 변경된_비밀번호) {
        Map<String, String> params = new HashMap<>();
        params.put("email", 회원.이메일);
        params.put("password", 변경된_비밀번호 + 회원.비밀번호);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value()).extract();
    }

    public static void 토큰_확인(String 발급된_토큰) {
        AssertionsForClassTypes.assertThat(발급된_토큰).isNotBlank();
    }
}
