package com.shop.core.storeManager.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.storeManager.domain.StoreManager;
import com.shop.core.storeManager.domain.StoreManagerRepository;
import com.shop.core.storeManager.domain.StoreManagerStatus;
import com.shop.core.storeManager.exception.DuplicateEmailException;
import com.shop.core.storeManager.presentation.dto.StoreManagerRequest;
import com.shop.core.storeManager.presentation.dto.StoreManagerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class StoreManagerService {

    private final StoreManagerRepository storeManagerRepository;

    public StoreManagerService(StoreManagerRepository storeManagerRepository) {
        this.storeManagerRepository = storeManagerRepository;
    }

    @Transactional
    public StoreManagerResponse createStoreManager(StoreManagerRequest request) {
        if (isEmailAlreadyRegistered(request.getEmail())) {
            throw new DuplicateEmailException(ErrorType.DUPLICATE_MEMBER_EMAIL);
        }

        StoreManager storeManager = new StoreManager(request.getEmail(), request.getPassword(), request.getPhoneNumber(), StoreManagerStatus.ACTIVE);
        storeManagerRepository.save(storeManager);

        return StoreManagerResponse.of(storeManager);
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return storeManagerRepository.findByEmail(email).isPresent();
    }
}
