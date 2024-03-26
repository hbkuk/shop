package com.shop.core.userSecurity.domain;

import com.shop.core.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@Getter
public class UserSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salt;

    @OneToOne
    private Member member;

    protected UserSecurity() {
    }

    public UserSecurity(String salt, Member member) {
        this.salt = salt;
        this.member = member;
    }
}
