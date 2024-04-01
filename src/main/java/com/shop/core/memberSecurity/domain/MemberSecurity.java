package com.shop.core.memberSecurity.domain;

import com.shop.core.member.domain.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSecurity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salt;

    @OneToOne
    private Member member;

    public MemberSecurity(String salt, Member member) {
        this.salt = salt;
        this.member = member;
    }
}
