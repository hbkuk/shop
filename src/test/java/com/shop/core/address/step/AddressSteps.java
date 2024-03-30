package com.shop.core.address.step;

import com.shop.core.address.presentation.dto.AddressRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressSteps {

    public static ExtractableResponse<Response> 주소록_등록_요청_토큰_포함(AddressRequest 주소록_등록_요청_정보, String 토큰) {
        return given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주소록_등록_요청_정보)
                .when()
                .post("/addresses")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static void 주소록_정보_확인(AddressRequest 확인할_주소록_정보, String 토큰) {
        JsonPath 응답_정보 = given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/addresses")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();

        주소록_일치_확인(확인할_주소록_정보, 응답_정보);
    }

    public static void 주소록_등록_요청_토큰_미포함(AddressRequest 주소록_등록_요청_정보) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주소록_등록_요청_정보)
                .when()
                .post("/addresses")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 주소록_수정_요청_토큰_포함(AddressRequest 주소록_수정_요청_정보, ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰) {
        given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주소록_수정_요청_정보)
                .when()
                .put("/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();
    }

    public static void 주소록_수정_확인(AddressRequest 확인할_주소록_정보, String 토큰) {
        JsonPath 응답_정보 = given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/addresses")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();

        // then
        주소록_일치_확인(확인할_주소록_정보, 응답_정보);
    }

    private static void 주소록_일치_확인(AddressRequest 확인할_주소록_정보, JsonPath jsonPath) {
        assertThat(jsonPath.getList("address", String.class)).containsExactly(확인할_주소록_정보.getAddress());
        assertThat(jsonPath.getList("detailedAddress", String.class)).containsExactly(확인할_주소록_정보.getDetailedAddress());
        assertThat(jsonPath.getList("description", String.class)).containsExactly(확인할_주소록_정보.getDescription());
        assertThat(jsonPath.getList("isDefault", boolean.class)).containsExactly(확인할_주소록_정보.getIsDefault());
    }

    public static void 주소록_수정_요청_토큰_미포함(AddressRequest 주소록_수정_요청_정보, ExtractableResponse<Response> 주소록_등록_요청_응답) {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주소록_수정_요청_정보)
                .when()
                .put("/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 기본_주소록_변경_요청_토큰_포함(ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰_정보) {
        given().log().all()
                .header("Authorization", 토큰_정보)
                .when()
                .put("/addresses/default/{id}", getCreatedLocationId(주소록_등록_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 기본_주소록_확인(ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰) {
        JsonPath jsonPath = given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();

        기본_주소록_확인(jsonPath, true);
    }

    private static void 기본_주소록_확인(JsonPath jsonPath, boolean expected) {
        assertThat(jsonPath.getBoolean("isDefault")).isEqualTo(expected);
    }

    public static void 기본_주소록_변경_요청_토큰_미포함(ExtractableResponse<Response> 주소록_등록_요청_응답) {
        given().log().all()
                .when()
                .put("/addresses/default/{id}", getCreatedLocationId(주소록_등록_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public static void 기본_주소록_아님_확인(ExtractableResponse<Response> 주소록_등록_요청_응답, String 토큰) {
        JsonPath jsonPath = given().log().all()
                .header("Authorization", 토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/addresses/{id}", getCreatedLocationId(주소록_등록_요청_응답))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath();

        기본_주소록_확인(jsonPath, false);
    }

}
