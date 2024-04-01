package com.shop.common.util;

import com.shop.core.admin.auth.application.dto.AdminGithubClientCodeRequest;
import com.shop.core.admin.auth.application.dto.GithubProfileResponse;
import com.shop.core.admin.auth.application.dto.GithubTokenResponse;
import com.shop.core.admin.auth.fixture.AdminGithubFixture;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
public class FakeAdminGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubTokenResponse> createToken(@RequestBody AdminGithubClientCodeRequest request) {
        GithubTokenResponse githubAccessTokenResponse = GithubTokenResponse.of(AdminGithubFixture.findTokenByCode(
                request.getCode()), "repo,gist", "bearer");
        return ResponseEntity.ok().body(githubAccessTokenResponse);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> findAuthenticateUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(new GithubProfileResponse(AdminGithubFixture.findEmailByToken(token)));
    }
}
