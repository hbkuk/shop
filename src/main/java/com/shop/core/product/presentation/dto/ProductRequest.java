package com.shop.core.product.presentation.dto;

import com.shop.core.product.domain.Product;
import com.shop.core.product.domain.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;

    private int salesCount;

    private String description;

    private int price;

    private Long storeId;

    public static ProductRequest of(String name, int salesCount, String description, int price, Long storeId) {
        return new ProductRequest(name, salesCount, description, price, storeId);
    }

    public Product toEntity(ProductStatus productStatus) {
        return new Product(name, salesCount, description, price, productStatus, storeId);
    }
}
