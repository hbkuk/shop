package com.shop.core.memberAuth.presentation;

import com.shop.common.exception.ErrorType;
import com.shop.core.memberAuth.application.JwtTokenProvider;
import com.shop.core.memberAuth.domain.LoginUser;
import com.shop.core.memberAuth.domain.NonLoginUser;
import com.shop.core.memberAuth.exception.InvalidTokenException;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@AllArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthenticationPrincipal annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        boolean required = annotation == null || annotation.required();

        String authorization = webRequest.getHeader("Authorization");
        if (!required) {
            if (authorization == null) {
                return new NonLoginUser();
            }
        }

        validateAuthorization(authorization);
        return new LoginUser(jwtTokenProvider.getPrincipal(getToken(authorization)));
    }

    private void validateAuthorization(String authorization) {
        if (authorization == null) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }
    }

    private String getToken(String authorization) {
        try {
            return authorization.split(" ")[1];
        } catch (Exception e) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }
    }
}
