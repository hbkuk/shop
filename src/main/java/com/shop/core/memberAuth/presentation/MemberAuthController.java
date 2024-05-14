package com.shop.core.memberAuth.presentation;

import com.shop.core.memberAuth.application.MemberAuthService;
import com.shop.core.memberAuth.application.dto.AuthRequest;
import com.shop.core.memberAuth.application.dto.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/login/member")
    public ResponseEntity<AuthResponse> createToken(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(memberAuthService.createToken(request.getEmail(), request.getPassword()));
    }
}
