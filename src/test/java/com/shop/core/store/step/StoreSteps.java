package com.shop.core.store.step;

import com.shop.core.store.presentation.dto.StoreRequest;
import com.shop.core.store.presentation.dto.StoreStatusRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StoreSteps {

    public static ExtractableResponse<Response> 성공하는_상점_등록_요청_토큰_포함(String 상점_관리자_토큰, StoreRequest 등록할_상점_정보) {
        return post_요청_토큰_포함(상점_관리자_토큰, "/store", 등록할_상점_정보, HttpStatus.CREATED);
    }

    public static void 실패하는_상점_등록_요청_토큰_미포함(StoreRequest 등록할_상점_정보) {
        post_요청_토큰_미포함("/store", 등록할_상점_정보, HttpStatus.UNAUTHORIZED);
    }

    public static void 상점_정상_등록_확인(String 상점_관리자_토큰, ExtractableResponse<Response> 상점_등록_요청_응답, StoreRequest 확인할_상점_등록_정보) {
        ExtractableResponse<Response> 상점_조회_요청_응답 = get_요청_토큰_포함(상점_관리자_토큰, "/store/{id}", getCreatedLocationId(상점_등록_요청_응답), HttpStatus.OK);

        assertThat(상점_조회_요청_응답.jsonPath().getString("name")).isEqualTo(확인할_상점_등록_정보.getName());
    }

    public static void 성공하는_상점_변경_요청_토큰_포함(String 상점_관리자_토큰, ExtractableResponse<Response> 상점_등록_요청_응답, StoreStatusRequest 변경할_상점_상태) {
        put_요청_토큰_포함(상점_관리자_토큰, "/store/{storeId}", getCreatedLocationId(상점_등록_요청_응답), 변경할_상점_상태, HttpStatus.OK);
    }

    public static void 실패하는_상점_변경_요청_토큰_미포함(ExtractableResponse<Response> 상점_등록_요청_응답, StoreStatusRequest 변경할_상점_상태) {
        put_요청_토큰_미포함("/store/{storeId}", getCreatedLocationId(상점_등록_요청_응답), 변경할_상점_상태, HttpStatus.UNAUTHORIZED);
    }

    public static void 상점_상태_정상_변경_확인(String 상점_관리자_토큰, ExtractableResponse<Response> 상점_등록_요청_응답, StoreStatusRequest 확인할_상점_상태) {
        ExtractableResponse<Response> 상점_조회_요청_응답 = get_요청_토큰_포함(상점_관리자_토큰, "/store/{id}", getCreatedLocationId(상점_등록_요청_응답), HttpStatus.OK);
        assertThat(상점_조회_요청_응답.jsonPath().getString("status")).isEqualTo(확인할_상점_상태.getStatus().toString());
    }
}
