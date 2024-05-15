package com.shop.core.store.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.store.domain.StoreStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StoreStatusRequest {

    private Long id;

    @JsonProperty("status")
    private StoreStatus status;

    public StoreStatusRequest(StoreStatus status) {
        this.status = status;
    }

    public static StoreStatusRequest mergeStoreId(Long storeId, StoreStatusRequest request) {
        return new StoreStatusRequest(storeId, request.getStatus());
    }

    public static StoreStatusRequest of(Long id, StoreStatus storeStatus) {
        return new StoreStatusRequest(id, storeStatus);
    }

    public static StoreStatusRequest of(StoreStatus storeStatus) {
        return new StoreStatusRequest(storeStatus);
    }
}
