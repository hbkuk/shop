package com.shop.core.storeManager.presentation;

import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import com.shop.core.storeManager.presentation.dto.StoreManagerResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@AllArgsConstructor
public class StoreManagerController {

    private final StoreManagerService storeManagerService;

    @PostMapping("/store-manager")
    public ResponseEntity<Void> createStoreManager(@RequestBody StoreManagerRequest request) {
        StoreManagerResponse storeManager = storeManagerService.createStoreManager(request);
        return ResponseEntity.created(URI.create("/storeManager/" + storeManager.getId())).build();
    }
}
