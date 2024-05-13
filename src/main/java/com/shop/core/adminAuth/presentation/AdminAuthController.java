package com.shop.core.adminAuth.presentation;

import com.shop.core.adminAuth.application.AdminAuthService;
import com.shop.core.adminAuth.presentation.dto.AdminAuthResponse;
import com.shop.core.adminAuth.presentation.dto.AdminGithubCodeRequest;
import com.shop.core.adminAuth.presentation.dto.AdminTokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/admin/login/github")
    public ResponseEntity<AdminAuthResponse> createToken(@RequestBody AdminGithubCodeRequest request) {
        AdminTokenResponse tokenByGithub = adminAuthService.createTokenByGithub(request.getCode());
        return ResponseEntity.ok(AdminAuthResponse.of(tokenByGithub.getAccessToken()));
    }
}
