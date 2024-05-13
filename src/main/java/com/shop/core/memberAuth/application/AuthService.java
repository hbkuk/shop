package com.shop.core.memberAuth.application;

import com.shop.core.memberAuth.application.dto.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse createToken(String email, String password) {
        userDetailsService.verifyUser(email, password);

        return AuthResponse.of(jwtTokenProvider.createToken(email));
    }

}
