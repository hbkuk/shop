package com.shop.core.memberSecurity.application;

import com.shop.common.annotation.ApplicationTest;
import com.shop.common.security.PasswordSecurityManager;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.application.dto.MemberResponse;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.Status;
import com.shop.core.member.domain.Type;
import com.shop.core.memberSecurity.domain.MemberSecurity;
import com.shop.core.memberSecurity.domain.MemberSecurityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.member.fixture.MemberFixture.브라운;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 보안 서비스 레이어 테스트")
@ApplicationTest
public class MemberSecurityServiceTest {

    @Autowired
    MemberSecurityService memberSecurityService;

    @Autowired
    MemberSecurityRepository memberSecurityRepository;

    @Autowired
    PasswordSecurityManager passwordSecurityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Nested
    class 비밀번호_보안_적용 {

        @Nested
        class 성공 {

            /**
              * Given 회원을 생성한다.
              * When  생성한 회원에 비밀번호 보안을 적용한다.
              * Then  정상적으로 보안이 적용된다.
              */
            @Test
            void 비밀번호_보안_적용() {
                // given
                MemberRequest 브라운_회원_생성_요청 =
                        MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, Type.NORMAL, Status.ACTIVE);
                Member 저장된_브라운_정보 = memberRepository.save(브라운_회원_생성_요청.toMember());

                // when
                memberSecurityService.applyPasswordSecurity(저장된_브라운_정보);

                // then
                MemberSecurity 저장된_보안_정보 = memberSecurityRepository.findByMember(저장된_브라운_정보);
                assertThat(저장된_보안_정보.getSalt()).isNotBlank();
            }
        }

    }

    @Nested
    class 비밀번호_검증 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성할 때, 비밀번호 보안이 적용된다.
             * When  회원을 생성할 때 사용된 암호화되지 않은 비밀번호를 통해 비밀번호를 검증할 경우
             * Then  일치한다.
             */
            @Test
            void 패스워드_확인_성공() {
                // given
                MemberRequest 브라운_회원_생성_요청 =
                        MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, Type.NORMAL, Status.ACTIVE);
                MemberResponse 생성된_브라운_회원 = memberService.createMember(브라운_회원_생성_요청);

                // when
                boolean 비밀번호_일치_여부 = memberSecurityService.verifyPassword(브라운.비밀번호, memberRepository.findById(생성된_브라운_회원.getId()).get());

                // then
                assertThat(비밀번호_일치_여부).isTrue();
            }
        }
        
        @Nested
        class 실패 {

            /**
             * Given 회원을 생성할 때, 비밀번호 보안이 적용된다.
             * When  회원을 생성할 때 사용된 비밀번호가 아닌 다른 비밀번호로 검증할 경우
             * Then  일치하지 않는다.
             */
            @Test
            void 패스워드_확인_실패() {
                // given
                MemberRequest 브라운_회원_생성_요청 =
                        MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, Type.NORMAL, Status.ACTIVE);
                MemberResponse 생성된_브라운_회원 = memberService.createMember(브라운_회원_생성_요청);

                // when
                boolean 비밀번호_일치_여부 = memberSecurityService.verifyPassword("random" + 브라운.비밀번호, memberRepository.findById(생성된_브라운_회원.getId()).get());

                // then
                assertThat(비밀번호_일치_여부).isFalse();
            }
        }
    }
}
