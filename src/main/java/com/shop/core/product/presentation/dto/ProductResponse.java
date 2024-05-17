package com.shop.core.product.presentation.dto;

import com.shop.core.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private int salesCount;

    private String description;

    private int price;

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getSalesCount(), product.getDescription(), product.getPrice());
    }
}
