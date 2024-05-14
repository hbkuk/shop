package com.shop.common.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(nullable = false, updatable = false, name = "CREATED_AT")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "MODIFIED_AT")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

