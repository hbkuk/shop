package com.shop.core.member.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.Status;
import com.shop.core.member.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {
    private String email;
    private String password;
    private int age;
    @JsonIgnore
    private Type type;
    @JsonIgnore
    private Status status;

    public Member toMember() {
        return new Member(email, password, age, type, status);
    }

    public Member toMember(Type type, Status status) {
        return new Member(email, password, age, type, status);
    }

    public MemberRequest(String password, Integer age) {
        this.password = password;
        this.age = age;
    }

    public static MemberRequest updateOf(String password, int age) {
        return new MemberRequest(password, age);
    }

    public static MemberRequest createOf(String email, String password, int age, Type type, Status status) {
        return new MemberRequest(email, password, age, type, status);
    }
}
