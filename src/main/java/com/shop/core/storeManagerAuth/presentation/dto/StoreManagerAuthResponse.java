package com.shop.core.storeManagerAuth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreManagerAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public static StoreManagerAuthResponse of(String token) {
        return new StoreManagerAuthResponse(token);
    }
}
