package com.shop.core.auth.presentation;

import com.shop.core.auth.application.AuthService;
import com.shop.core.auth.application.dto.AuthRequest;
import com.shop.core.auth.application.dto.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/token")
    public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.createToken(request.getEmail(), request.getPassword()));
    }
}
