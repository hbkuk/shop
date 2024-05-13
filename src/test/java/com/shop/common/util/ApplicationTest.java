package com.shop.common.util;

import com.shop.core.adminAuth.domain.*;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ApplicationTest {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MemberRepository memberRepository;

    public Admin 관리자_생성(String email, String phoneNumber, AdminRole role, AdminSignupChannel signupChannel, AdminStatus status) {
        return adminRepository.save(new Admin(email, phoneNumber, role, signupChannel, status));
    }

    public Member 회원_생성(String email, String password, int age, MemberType memberType, MemberStatus memberStatus) {
        return memberRepository.save(new Member(email, password, age, memberType, memberStatus));
    }

    public List<Member> 랜덤_회원_목록_생성(int numberOfMembers, MemberType memberType, MemberStatus memberStatus) {
        List<Member> members = new ArrayList<>();

        IntStream.range(0, numberOfMembers).forEach(number -> {
            String 랜덤_이메일 = 랜덤_이메일_생성();
            String 랜덤_비밀번호 = UUID.randomUUID().toString();
            int 랜덤_나이 = 랜덤_나이_생성();

            members.add(memberRepository.save(new Member(랜덤_이메일, 랜덤_비밀번호, 랜덤_나이, memberType, memberStatus)));
        });
        return members;
    }

    private String 랜덤_이메일_생성() {
        Random random = new Random();

        String[] 랜덤_이메일_도메인 = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com"};

        return UUID.randomUUID().toString() + "@" + 랜덤_이메일_도메인[random.nextInt(랜덤_이메일_도메인.length)];
    }

    private int 랜덤_나이_생성() {
        Random random = new Random();

        return random.nextInt(80) + 10;
    }

}
