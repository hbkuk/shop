package com.shop.common.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class RestAssuredTemplate {

    public static ExtractableResponse<Response> get_요청_토큰_포함(String 토큰_정보, String 경로, Object 파라미터, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .header("Authorization", 토큰_정보)
                .when()
                .get(경로, 파라미터)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }

    public static ExtractableResponse<Response> post_요청_토큰_포함(String 토큰_정보, String 경로, Object 파라미터, Object 요청_정보, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .header("Authorization", 토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_정보)
                .when()
                .post(경로, 파라미터)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }

    public static ExtractableResponse<Response> post_요청_토큰_포함(String 토큰_정보, String 경로, Object 요청_정보, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .header("Authorization", 토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_정보)
                .when()
                .post(경로)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }

    public static ExtractableResponse<Response> post_요청_토큰_미포함(String 경로, Object 요청_정보, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_정보)
                .when()
                .post(경로)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }

    public static ExtractableResponse<Response> post_요청_토큰_미포함(String 경로, Object 파라미터, Object 요청_정보, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_정보)
                .when()
                .post(경로, 파라미터)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }

    public static ExtractableResponse<Response> put_요청_토큰_포함(String 토큰_정보, String 경로, Object 파라미터, Object 요청_정보, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .header("Authorization", 토큰_정보)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_정보)
                .when()
                .put(경로, 파라미터)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }

    public static ExtractableResponse<Response> put_요청_토큰_미포함(String 경로, Object 파라미터, Object 요청_정보, HttpStatus 예상하는_상태코드) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(요청_정보)
                .when()
                .put(경로, 파라미터)
                .then().log().all()
                .statusCode(예상하는_상태코드.value())
                .extract();
    }
}
