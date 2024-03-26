package com.shop.core.member.step;

import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.fixture.MemberFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberSteps {

    public static void 성공하는_회원_생성_요청(String 이메일, String 비밀번호, int 나이) {
        ExtractableResponse<Response> 회원_생성_요청 = 회원_생성_요청(이메일, 비밀번호, 나이);
        회원_생성_확인(회원_생성_요청);
    }

    public static ExtractableResponse<Response> 회원_생성_요청(String 이메일, String 비밀번호, int 나이) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(이메일, 비밀번호, 나이))
                .when().post("/members")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 회원_생성_요청(MemberFixture memberFixture) {
        return 회원_생성_요청(memberFixture.이메일, memberFixture.비밀번호, memberFixture.나이);
    }

    public static void 회원_생성_확인(ExtractableResponse<Response> 회원_생성_응답) {
        assertThat(회원_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_생성_실패_확인(ExtractableResponse<Response> 회원_생성_응답) {
        assertThat(회원_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}