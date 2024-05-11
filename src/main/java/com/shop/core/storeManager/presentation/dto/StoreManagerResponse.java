package com.shop.core.storeManager.presentation.dto;

import com.shop.core.storeManager.domain.StoreManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreManagerResponse {
    private Long id;
    private String email;
    private String phoneNumber;

    public static StoreManagerResponse of(StoreManager manager) {
        return new StoreManagerResponse(manager.getId(), manager.getEmail(), manager.getPhoneNumber());
    }
}
