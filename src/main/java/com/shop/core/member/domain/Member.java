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

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Member(String email, String password, Integer age, Type type, Status status) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.type = type;
        this.status = status;
    }

    public void update(Member member) {
        this.password = member.password;
        this.age = member.age;
    }

    public Member updateStatus(Status status) {
        this.status = status;
        return this;
    }

    public Member updateEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
        return this;
    }
}
