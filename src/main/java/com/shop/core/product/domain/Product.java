package com.shop.core.product.domain;

import com.shop.common.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private int salesCount;

    private String description;

    private int price;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @JoinColumn(name = "store_id")
    private Long storeId;

    public Product(String name, int salesCount, String description, int price, ProductStatus productStatus, Long storeId) {
        this.name = name;
        this.salesCount = salesCount;
        this.description = description;
        this.price = price;
        this.productStatus = productStatus;
        this.storeId = storeId;
    }
}
