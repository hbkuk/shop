package com.shop.core.store.domain;

import com.shop.common.domain.base.BaseEntity;
import com.shop.core.storeManager.domain.StoreManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String content;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @JoinColumn(name = "STORE_MANAGER_EMAIL")
    private String storeManagerEmail;

    public Store(String name, String content, StoreStatus status, String storeManagerEmail) {
        this.name = name;
        this.content = content;
        this.status = status;
        this.storeManagerEmail = storeManagerEmail;
    }

    public boolean isOwn(StoreManager storeManager) {
        return storeManagerEmail.equals(storeManager.getEmail());
    }

    public Store updateStatus(StoreStatus status) {
        // TODO: 변경해도 되는지 확인

        this.status = status;
        return this;
    }
}
