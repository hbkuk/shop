package com.shop.core.member.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode
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

    protected Member() {
    }

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
