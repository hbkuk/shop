package com.shop.core.store.presentation;

import com.shop.common.auth.AuthenticationPrincipal;
import com.shop.common.domain.auth.LoginUser;
import com.shop.core.store.application.StoreService;
import com.shop.core.store.presentation.dto.StoreRequest;
import com.shop.core.store.presentation.dto.StoreResponse;
import com.shop.core.store.presentation.dto.StoreStatusRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/store")
    public ResponseEntity<StoreResponse> createStore(@RequestBody StoreRequest request,
                                                     @AuthenticationPrincipal LoginUser loginUser) {
        StoreResponse response = storeService.createStore(request, loginUser);
        return ResponseEntity.created(URI.create("/store/" + response.getId())).build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<StoreResponse> findById(@PathVariable Long storeId,
                                                  @AuthenticationPrincipal LoginUser loginUser) {
        StoreResponse response = storeService.findById(storeId, loginUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/store/{storeId}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long storeId,
                                             @RequestBody StoreStatusRequest request,
                                             @AuthenticationPrincipal LoginUser loginUser) {
        storeService.updateStatus(StoreStatusRequest.mergeStoreId(storeId, request), loginUser);
        return ResponseEntity.ok().build();
    }
}
