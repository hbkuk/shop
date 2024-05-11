package com.shop.core.storeManagerSecurity.domain;

import com.shop.core.storeManager.domain.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreManagerSecurityRepository extends JpaRepository<StoreManagerSecurity, Long> {


    @Query("SELECT s FROM StoreManagerSecurity s WHERE s.storeManager = :storeManager")
    StoreManagerSecurity findByStoreManager(@Param("storeManager") StoreManager storeManager);

}
