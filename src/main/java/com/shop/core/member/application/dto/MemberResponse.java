package com.shop.core.member.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.core.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private int age;

    public MemberResponse(String password, int age) {
        this.password = password;
        this.age = age;
    }

    public MemberResponse(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(String password, int age) {
        return new MemberResponse(password, age);
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }
}