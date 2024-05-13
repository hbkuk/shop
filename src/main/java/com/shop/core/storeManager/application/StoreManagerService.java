package com.shop.core.storeManager.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.storeManager.domain.StoreManager;
import com.shop.core.storeManager.domain.StoreManagerRepository;
import com.shop.core.storeManager.domain.StoreManagerStatus;
import com.shop.core.storeManager.exception.DuplicateEmailException;
import com.shop.core.storeManager.exception.NotFoundStoreManagerException;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import com.shop.core.storeManager.presentation.dto.StoreManagerResponse;
import com.shop.core.storeManagerSecurity.application.StoreManagerSecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class StoreManagerService {

    private final StoreManagerRepository storeManagerRepository;
    private final StoreManagerSecurityService storeManagerSecurityService;

    public StoreManagerService(StoreManagerRepository storeManagerRepository, StoreManagerSecurityService storeManagerSecurityService) {
        this.storeManagerRepository = storeManagerRepository;
        this.storeManagerSecurityService = storeManagerSecurityService;
    }

    @Transactional
    public StoreManagerResponse createStoreManager(StoreManagerRequest request) {
        if (isEmailAlreadyRegistered(request.getEmail())) {
            throw new DuplicateEmailException(ErrorType.DUPLICATE_MEMBER_EMAIL);
        }

        StoreManager storeManager = new StoreManager(request.getEmail(), request.getPassword(), request.getPhoneNumber(), StoreManagerStatus.ACTIVE);
        storeManagerSecurityService.applySecurity(storeManager);
        storeManagerRepository.save(storeManager);

        return StoreManagerResponse.of(storeManager);
    }

    public boolean verifyUser(String email, String password) {
        StoreManager storeManager = findByEmail(email);
        return storeManagerSecurityService.verifyPassword(password, storeManager);
    }

    public StoreManagerResponse findManager(String email) {
        return StoreManagerResponse.of(findByEmail(email));
    }

    public StoreManager findByEmail(String email) {
        return storeManagerRepository.findByEmail(email).orElseThrow(() -> new NotFoundStoreManagerException(ErrorType.NOT_FOUND_STORE_MANAGER));
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return storeManagerRepository.findByEmail(email).isPresent();
    }
}
