package com.shop.core.memberAuth.application;

import com.shop.common.auth.JwtTokenProvider;
import com.shop.core.memberAuth.application.dto.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberAuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberAuthService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse createToken(String email, String password) {
        userDetailsService.verifyUser(email, password);

        return AuthResponse.of(jwtTokenProvider.createToken(email));
    }

}
