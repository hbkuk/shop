package com.shop.core.memberAuth.step;

import com.shop.core.member.fixture.MemberFixture;
import com.shop.core.memberAuth.application.dto.AuthRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.RestAssuredTemplate.post_요청_토큰_미포함;
import static com.shop.core.member.step.MemberSteps.회원_생성_요청;

public class AuthSteps {
    public static String 성공하는_토큰_발급_요청(MemberFixture 회원) {
        AuthRequest 인증_요청_정보 = AuthRequest.of(회원.이메일, 회원.비밀번호);
        ExtractableResponse<Response> 응답 = post_요청_토큰_미포함("/login/member", 인증_요청_정보, HttpStatus.OK);

        return 응답.jsonPath().getString("access_token");
    }

    public static void 실패하는_토큰_발급_요청(MemberFixture 회원) {
        AuthRequest 인증_요청_정보 = AuthRequest.of(회원.이메일, 회원.비밀번호);
        post_요청_토큰_미포함("/login/member", 인증_요청_정보, HttpStatus.BAD_REQUEST);
    }

    public static void 실패하는_토큰_발급_요청(MemberFixture 회원, String 변경된_비밀번호) {
        AuthRequest 인증_요청_정보 = AuthRequest.of(회원.이메일, 변경된_비밀번호 + 회원.비밀번호);
        post_요청_토큰_미포함("/login/member", 인증_요청_정보, HttpStatus.BAD_REQUEST);
    }

    public static void 토큰_확인(String 발급된_토큰) {
        AssertionsForClassTypes.assertThat(발급된_토큰).isNotBlank();
    }

    public static String 회원생성_후_토큰_발급(MemberFixture memberFixture) {
        회원_생성_요청(memberFixture);
        return "Bearer " + 성공하는_토큰_발급_요청(memberFixture);
    }
}
