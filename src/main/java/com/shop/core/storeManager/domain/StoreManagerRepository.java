package com.shop.core.storeManager.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreManagerRepository extends JpaRepository<StoreManager, Long> {
    Optional<StoreManager> findByEmail(String email);
}
