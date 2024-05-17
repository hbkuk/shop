package com.shop.core.product.step;

import com.shop.core.product.presentation.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static com.shop.common.util.HttpResponseUtil.getCreatedLocationId;
import static com.shop.common.util.RestAssuredTemplate.*;

public class ProductSteps {

    public static ExtractableResponse<Response> 성공하는_상품_등록_요청(String 상점_관리자_토큰, ProductRequest 상품_등록_정보) {
        return post_요청_토큰_포함(상점_관리자_토큰, "/products", 상품_등록_정보, HttpStatus.CREATED);
    }

    public static void 실패하는_상품_등록_요청(ProductRequest 상품_등록_정보) {
        post_요청_토큰_미포함("/products", 상품_등록_정보, HttpStatus.UNAUTHORIZED);
    }


    public static void 상품_정상_등록_확인(String 상점_관리자_토큰, ExtractableResponse<Response> 상품_등록_요청_응답) {
        get_요청_토큰_포함(상점_관리자_토큰, "/products/{id}", getCreatedLocationId(상품_등록_요청_응답), HttpStatus.OK);
    }
}
