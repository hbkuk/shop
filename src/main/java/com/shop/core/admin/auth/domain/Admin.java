package com.shop.core.admin.auth.domain;

import com.shop.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "ADMIN_EMAIL")
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    private AdminSignupChannel signupChannel;

    @Enumerated(EnumType.STRING)
    private AdminStatus status;

    public Admin(String email, String phoneNumber, AdminRole role, AdminSignupChannel signupChannel, AdminStatus status) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.signupChannel = signupChannel;
        this.status = status;
    }

    public boolean isSameSignupChannel(AdminSignupChannel signupChannel) {
        return this.signupChannel.equals(signupChannel);
    }
}
