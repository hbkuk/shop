package com.shop.core.storeManagerSecurity.domain;

import com.shop.common.domain.base.BaseEntity;
import com.shop.core.storeManager.domain.StoreManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor
public class StoreManagerSecurity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salt;

    @OneToOne
    private StoreManager storeManager;

    public StoreManagerSecurity(String salt, StoreManager storeManager) {
        this.salt = salt;
        this.storeManager = storeManager;
    }
}
