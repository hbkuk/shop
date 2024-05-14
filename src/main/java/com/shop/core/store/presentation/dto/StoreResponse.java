package com.shop.core.store.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.store.domain.Store;
import com.shop.core.store.domain.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {

    private Long id;

    private String name;

    private String content;

    @JsonProperty("status")
    private StoreStatus storeStatus;

    public static StoreResponse of(Store store) {
        return new StoreResponse(store.getId(), store.getName(), store.getContent(), store.getStatus());
    }
}
