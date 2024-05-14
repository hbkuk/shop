package com.shop.core.store.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    private String name;

    private String content;

    public static StoreRequest of(String name, String content) {
        return new StoreRequest(name, content);
    }
}
