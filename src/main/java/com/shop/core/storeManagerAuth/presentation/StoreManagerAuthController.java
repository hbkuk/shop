package com.shop.core.storeManagerAuth.presentation;

import com.shop.core.storeManagerAuth.application.StoreManagerAuthService;
import com.shop.core.storeManagerAuth.presentation.dto.StoreManagerAuthRequest;
import com.shop.core.storeManagerAuth.presentation.dto.StoreManagerAuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class StoreManagerAuthController {

    private final StoreManagerAuthService storeManagerAuthService;

    @PostMapping("/login/store-manager")
    public ResponseEntity<StoreManagerAuthResponse> createToken(@RequestBody StoreManagerAuthRequest request) {
        return ResponseEntity.ok(storeManagerAuthService.createToken(request.getEmail(), request.getPassword()));
    }
}
