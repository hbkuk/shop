package com.shop.common.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

public class HttpResponseUtil {

    public static Long getCreatedLocationId(ExtractableResponse<Response> createResponse) {
        return Long.parseLong(createResponse.header(HttpHeaders.LOCATION)
                .substring(createResponse.header(HttpHeaders.LOCATION).lastIndexOf('/') + 1));
    }
}
