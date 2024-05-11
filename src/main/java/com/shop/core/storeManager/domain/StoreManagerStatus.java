package com.shop.core.storeManager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreManagerStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    WITHDRAWN;
}
