package com.shop.core.storeManagerAuth.application;

import com.shop.common.auth.JwtTokenProvider;
import com.shop.common.exception.ErrorType;
import com.shop.core.member.exception.PasswordMismatchException;
import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManagerAuth.presentation.dto.StoreManagerAuthResponse;
import org.springframework.stereotype.Service;

@Service
public class StoreManagerAuthService {

    private final StoreManagerService storeManagerService;
    private final JwtTokenProvider jwtTokenProvider;

    public StoreManagerAuthService(StoreManagerService storeManagerService, JwtTokenProvider jwtTokenProvider) {
        this.storeManagerService = storeManagerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public StoreManagerAuthResponse createToken(String email, String password) {
        if (!storeManagerService.verifyUser(email, password)) {
            throw new PasswordMismatchException(ErrorType.PASSWORD_MISMATCH);
        }

        return StoreManagerAuthResponse.of(jwtTokenProvider.createToken(email));
    }
}
