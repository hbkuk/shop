package com.shop.core.storeManager.domain;

import com.shop.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreManager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_MANAGER_ID")
    private Long id;

    @Column(unique = true, name = "STORE_MANAGER_EMAIL")
    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private StoreManagerStatus status;

    public StoreManager(String email, String password, String phoneNumber, StoreManagerStatus status) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
}
