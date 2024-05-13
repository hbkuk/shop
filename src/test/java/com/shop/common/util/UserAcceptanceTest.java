package com.shop.common.util;

import com.shop.core.adminAuth.domain.*;
import com.shop.core.adminAuth.fixture.AdminGithubFixture;
import com.shop.core.adminAuth.presentation.dto.AdminGithubCodeRequest;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.adminAuth.step.AdminAuthSteps.깃허브_토큰_발급_요청;

public class UserAcceptanceTest extends AcceptanceTest {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    MemberRepository memberRepository;

    public String 관리자_생성_후_토큰_발급(AdminGithubFixture 관리자_깃허브_정보) {
        관리자_생성(관리자_깃허브_정보.email, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

        return 깃허브_코드로_토큰_발급(관리자_깃허브_정보.code);
    }

    public void 관리자_등록(Admin 관리자_정보) {
        adminRepository.save(관리자_정보);
    }

    public String 깃허브_코드로_토큰_발급(String 깃허브_코드) {
        var 깃허브_코드_정보 = AdminGithubCodeRequest.of(깃허브_코드);
        var 깃허브_토큰_발급_정보 = 깃허브_토큰_발급_요청(깃허브_코드_정보);

        return "Bearer " + 깃허브_토큰_발급_정보.jsonPath().getString("access_token");
    }

    public Admin 관리자_생성(String email, AdminRole role, AdminSignupChannel signupChannel, AdminStatus status) {
        return adminRepository.save(new Admin(email, "010-1234-5678", role, signupChannel, status));
    }

    public Member 회원_생성(String email, String password, int age, MemberType memberType, MemberStatus memberStatus) {
        return memberRepository.save(new Member(email, password, age, memberType, memberStatus));
    }
}
