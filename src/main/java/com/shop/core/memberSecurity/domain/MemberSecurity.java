package com.shop.core.memberSecurity.domain;

import com.shop.core.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@Getter
public class MemberSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salt;

    @OneToOne
    private Member member;

    protected MemberSecurity() {
    }

    public MemberSecurity(String salt, Member member) {
        this.salt = salt;
        this.member = member;
    }
}
