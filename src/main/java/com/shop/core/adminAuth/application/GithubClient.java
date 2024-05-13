package com.shop.core.adminAuth.application;

import com.shop.common.util.WebClientUtil;
import com.shop.core.adminAuth.application.dto.AdminGithubAccessTokenResponse;
import com.shop.core.adminAuth.application.dto.AdminGithubClientCodeRequest;
import com.shop.core.adminAuth.application.dto.AdminGithubProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class GithubClient {
    @Value("${github.base-url}")
    private String baseUrl;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    private final WebClientUtil webClientUtil;

    public GithubClient(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
    }

    public AdminGithubProfileResponse requestGithubProfile(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, token);

        return webClientUtil
                .get(baseUrl + "user", headers, AdminGithubProfileResponse.class)
                .block();
    }

    public String requestGithubToken(String code) {
        return webClientUtil
                .post(baseUrl + "/login/oauth/access_token",
                        createAccessTokenRequest(code),
                        MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE),
                        AdminGithubAccessTokenResponse.class)
                .block() // TODO: nonBlocking 변경
                .getAccessToken();
    }

    private AdminGithubClientCodeRequest createAccessTokenRequest(String code) {
        return new AdminGithubClientCodeRequest(
                code,
                clientId,
                clientSecret
        );
    }
}
