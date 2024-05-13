package com.shop.common.config;

import com.shop.core.memberAuth.application.JwtTokenProvider;
import com.shop.core.memberAuth.presentation.AuthenticationPrincipalArgumentResolver;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
    }
}
