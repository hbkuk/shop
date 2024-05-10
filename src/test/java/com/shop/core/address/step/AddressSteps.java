package com.shop.core.address.step;

import com.shop.core.address.presentation.dto.AddressRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressSteps {

    public static ExtractableResponse<Response> 주소록_등록_요청_토큰_포함(AddressRequest 주소록_등록_요청_정보, String 토큰) {
        return post_요청_토큰_포함(토큰, "/addresses", 주소록_등록_요청_정보, HttpStatus.CREATED);
    }

    public static void 주소록_정보_확인(AddressRequest 확인할_주소록_정보, String 토큰) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰, "/addresses", HttpStatus.OK);

        주소록_일치_확인(확인할_주소록_정보, 응답.jsonPath());
    }

    public static void 주소록_등록_요청_토큰_미포함(AddressRequest 주소록_등록_요청_정보) {
        post_요청_토큰_미포함("/addresses", 주소록_등록_요청_정보, HttpStatus.UNAUTHORIZED);
    }

    public static void 주소록_수정_요청_토큰_포함(AddressRequest 주소록_수정_요청_정보, ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰) {
        put_요청_토큰_포함(토큰, "/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답), 주소록_수정_요청_정보, HttpStatus.OK);
    }

    public static void 주소록_수정_확인(AddressRequest 확인할_주소록_정보, String 토큰) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰, "/addresses", HttpStatus.OK);

        // then
        주소록_일치_확인(확인할_주소록_정보, 응답.jsonPath());
    }

    public static void 주소록_수정_요청_토큰_미포함(AddressRequest 주소록_수정_요청_정보, ExtractableResponse<Response> 주소록_등록_요청_응답) {
        put_요청_토큰_미포함("/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답), 주소록_수정_요청_정보, HttpStatus.UNAUTHORIZED);
    }

    public static void 기본_주소록_변경_요청_토큰_포함(ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰_정보) {
        put_요청_토큰_포함(토큰_정보, "/addresses/default/{id}", getCreatedLocationId(주소록_등록_요청_응답), HttpStatus.OK);
    }

    public static void 기본_주소록_확인(ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰, "/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답), HttpStatus.OK);

        기본_주소록_확인(응답.jsonPath(), true);
    }

    public static void 기본_주소록_변경_요청_토큰_미포함(ExtractableResponse<Response> 주소록_등록_요청_응답) {
        put_요청_토큰_미포함("/addresses/default/{id}", getCreatedLocationId(주소록_등록_요청_응답), HttpStatus.UNAUTHORIZED);
    }

    public static void 기본_주소록_아님_확인(ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰) {
        ExtractableResponse<Response> 응답 = get_요청_토큰_포함(토큰, "/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답), HttpStatus.OK);

        기본_주소록_확인(응답.jsonPath(), false);
    }

    private static void 기본_주소록_확인(JsonPath jsonPath, boolean expected) {
        assertThat(jsonPath.getBoolean("isDefault")).isEqualTo(expected);
    }

    private static void 주소록_일치_확인(AddressRequest 확인할_주소록_정보, JsonPath jsonPath) {
        assertThat(jsonPath.getList("address", String.class)).containsExactly(확인할_주소록_정보.getAddress());
        assertThat(jsonPath.getList("detailedAddress", String.class)).containsExactly(확인할_주소록_정보.getDetailedAddress());
        assertThat(jsonPath.getList("description", String.class)).containsExactly(확인할_주소록_정보.getDescription());
        assertThat(jsonPath.getList("isDefault", boolean.class)).containsExactly(확인할_주소록_정보.getIsDefault());
    }

}
