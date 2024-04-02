package com.shop.core.member.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
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
    private MemberType memberType;
    @JsonIgnore
    private MemberStatus memberStatus;

    public Member toMember() {
        return new Member(email, password, age, memberType, memberStatus);
    }

    public Member toMember(MemberType memberType, MemberStatus memberStatus) {
        return new Member(email, password, age, memberType, memberStatus);
    }

    public MemberRequest(String password, Integer age) {
        this.password = password;
        this.age = age;
    }

    public static MemberRequest updateOf(String password, int age) {
        return new MemberRequest(password, age);
    }

    public static MemberRequest createOf(String email, String password, int age, MemberType memberType, MemberStatus memberStatus) {
        return new MemberRequest(email, password, age, memberType, memberStatus);
    }
}
