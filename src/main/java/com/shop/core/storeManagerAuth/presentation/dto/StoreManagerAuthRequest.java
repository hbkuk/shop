package com.shop.core.storeManagerAuth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreManagerAuthRequest {

    private String email;
    private String password;

    public static StoreManagerAuthRequest of(String email, String password) {
        return new StoreManagerAuthRequest(email, password);
    }
}
