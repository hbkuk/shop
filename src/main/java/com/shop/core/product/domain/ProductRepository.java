package com.shop.core.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.storeId= :storeId")
    Product findByStoreId(@Param("storeId") Long storeId);

    Optional<Product> findByName(String name);
}
