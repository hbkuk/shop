package com.shop.common.util;

import com.shop.core.admin.auth.domain.*;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

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
}
