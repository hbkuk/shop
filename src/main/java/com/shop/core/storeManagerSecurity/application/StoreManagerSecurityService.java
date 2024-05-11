package com.shop.core.storeManagerSecurity.application;

import com.shop.common.security.PasswordSecurityManager;
import com.shop.core.storeManager.domain.StoreManager;
import com.shop.core.storeManagerSecurity.domain.StoreManagerSecurity;
import com.shop.core.storeManagerSecurity.domain.StoreManagerSecurityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shop.common.util.SecurityUtil.createSalt;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StoreManagerSecurityService {

    private final StoreManagerSecurityRepository storeManagerSecurityRepository;
    private final PasswordSecurityManager passwordSecurityManager;

    @Transactional
    public void applySecurity(StoreManager storeManager) {
        StoreManagerSecurity storeManagerSecurity = new StoreManagerSecurity(createSalt(), storeManager);

        String encryptedPassword = passwordSecurityManager.encryptedPassword(storeManager.getPassword(), storeManagerSecurity.getSalt());
        storeManager.updateEncodedPassword(encryptedPassword);

        storeManagerSecurityRepository.save(storeManagerSecurity);
    }

    public boolean verifyPassword(String password, StoreManager storeManager) {
        StoreManagerSecurity storeManagerSecurity = storeManagerSecurityRepository.findByStoreManager(storeManager);
        return passwordSecurityManager.isPasswordMatchWithSalt(password, storeManagerSecurity.getSalt(), storeManager.getPassword());
    }
}
