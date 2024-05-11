package com.shop.core.storeManager.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreManagerRequest {

    private String email;

    private String password;

    private String phoneNumber;

    public static StoreManagerRequest of(String email, String password, String phoneNumber) {
        return new StoreManagerRequest(email, password, phoneNumber);
    }
}
