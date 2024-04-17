package com.shop.core.member.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true, name = "MEMBER_EMAIL")
    private String email;

    private String password;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    public Member(String email, String password, Integer age, MemberType memberType, MemberStatus memberStatus) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.memberType = memberType;
        this.memberStatus = memberStatus;
    }

    public void update(Member member) {
        this.password = member.password;
        this.age = member.age;
    }

    public Member updateStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
        return this;
    }

    public Member updateEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
        return this;
    }
}
