package com.shop.core.store.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreManagerEmail(String email);

    Optional<Store> findByName(String name);
}
