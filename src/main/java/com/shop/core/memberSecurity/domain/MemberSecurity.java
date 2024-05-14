package com.shop.core.memberSecurity.domain;

import com.shop.common.domain.base.BaseEntity;
import com.shop.core.member.domain.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSecurity extends BaseEntity {

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
