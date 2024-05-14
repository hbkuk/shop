package com.shop.core.store.application;

import com.shop.common.domain.auth.LoginUser;
import com.shop.common.exception.ErrorType;
import com.shop.core.store.domain.Store;
import com.shop.core.store.domain.StoreRepository;
import com.shop.core.store.domain.StoreStatus;
import com.shop.core.store.exception.DuplicateStoreCreationException;
import com.shop.core.store.exception.DuplicateStoreNameException;
import com.shop.core.store.exception.NonMatchingStoreManagerException;
import com.shop.core.store.exception.NotFoundStoreException;
import com.shop.core.store.presentation.dto.StoreRequest;
import com.shop.core.store.presentation.dto.StoreResponse;
import com.shop.core.store.presentation.dto.StoreStatusRequest;
import com.shop.core.storeManager.application.StoreManagerService;
import com.shop.core.storeManager.domain.StoreManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class StoreService {

    private final StoreManagerService storeManagerService;

    private final StoreRepository storeRepository;

    public StoreService(StoreManagerService storeManagerService, StoreRepository storeRepository) {
        this.storeManagerService = storeManagerService;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public StoreResponse createStore(StoreRequest request, LoginUser loginUser) {
        StoreManager storeManager = storeManagerService.findByEmail(loginUser.getEmail());

        if (storeRepository.findByStoreManagerEmail(storeManager.getEmail()).isPresent()) {
            throw new DuplicateStoreCreationException(ErrorType.DUPLICATE_STORE_CREATION);
        }

        if (storeRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateStoreNameException(ErrorType.DUPLICATE_STORE_NAME);
        }

        Store store = new Store(request.getName(), request.getContent(), StoreStatus.OPEN, storeManager.getEmail());
        return StoreResponse.of(storeRepository.save(store));
    }

    public StoreResponse findById(Long storeId, LoginUser loginUser) {
        StoreManager storeManager = storeManagerService.findByEmail(loginUser.getEmail());

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundStoreException(ErrorType.NOT_FOUND_STORE));
        if (!store.isOwn(storeManager)) {
            throw new NonMatchingStoreManagerException(ErrorType.NON_MATCHING_STORE_MANAGER);
        }

        return StoreResponse.of(store);
    }

    @Transactional
    public void updateStatus(StoreStatusRequest storeStatusRequest, LoginUser loginUser) {
        StoreManager storeManager = storeManagerService.findByEmail(loginUser.getEmail());

        Optional<Store> store = storeRepository.findById(storeStatusRequest.getId());
        if (!store.isPresent()) {
            throw new NotFoundStoreException(ErrorType.NOT_FOUND_STORE);
        }

        if (!store.get().isOwn(storeManager)) {
            throw new NonMatchingStoreManagerException(ErrorType.NON_MATCHING_STORE_MANAGER);
        }

        store.get().updateStatus(storeStatusRequest.getStatus());
    }
}
