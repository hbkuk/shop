package com.shop.core.member.application;

import com.shop.common.annotation.ApplicationTest;
import com.shop.common.exception.ErrorType;
import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.application.dto.MemberResponse;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.Status;
import com.shop.core.member.domain.Type;
import com.shop.core.member.exception.DuplicateEmailException;
import com.shop.core.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("회원 서비스 레이어 테스트")
@ApplicationTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SecurityService securityService;

    @Nested
    class 회원_생성 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성한다.
             * When  생성한 회원을 조회할 경우
             * Then  회원이 조회된다.
             */
            @Test
            @DisplayName("회원을 생성한다.")
            void 회원_생성() {
                // given
                MemberRequest 브라운_회원_생성_요청 = MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, Type.NORMAL, Status.ACTIVE);

                // when
                memberService.createMember(브라운_회원_생성_요청);

                // then
                assertThat(memberRepository.findByEmail(브라운.이메일).get())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(브라운_회원_생성_요청);
            }

        }

        @Nested
        class 실패 {

            @BeforeEach
            void 사전_회원_생성() {
                memberService.createMember(MemberRequest.createOf(스미스.이메일, 스미스.비밀번호, 스미스.나이, Type.NORMAL, Status.ACTIVE));
                memberService.createMember(MemberRequest.createOf(존슨.이메일, 존슨.비밀번호, 존슨.나이, Type.NORMAL, Status.ACTIVE));
                memberService.createMember(MemberRequest.createOf(윌리엄스.이메일, 윌리엄스.비밀번호, 윌리엄스.나이, Type.NORMAL, Status.ACTIVE));
                memberService.createMember(MemberRequest.createOf(브라운.이메일, 브라운.비밀번호, 브라운.나이, Type.NORMAL, Status.ACTIVE));
                memberService.createMember(MemberRequest.createOf(잭슨.이메일, 잭슨.비밀번호, 잭슨.나이, Type.NORMAL, Status.ACTIVE));
            }

            /**
             * Given 여러 회원을 생성한다.
             * When  기존 생성한 회원의 이메일과 동일한 회원을 생성할 경우
             * Then  회원을 생성할 수 없다.
             */
            @Test
            @DisplayName("회원을 생성할 수 없다.")
            void 회원_생성() {
                // given
                MemberRequest 기존_브라운과_동일한_이메일의_회원 = MemberRequest.createOf(브라운.이메일, "password1!", 20, Type.NORMAL, Status.ACTIVE);

                // when, then
                assertThatExceptionOfType(DuplicateEmailException.class)
                        .isThrownBy(() -> {
                            memberService.createMember(기존_브라운과_동일한_이메일의_회원);
                        }).withMessageMatching(ErrorType.DUPLICATE_MEMBER_EMAIL.getMessage());

            }
        }

    }

    @Nested
    class 회원_조회 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성한다.
             * When  생성된 회원의 번호로 회원을 조회할 경우
             * Then  생성된 회원이 조회된다.
             */
            @Test
            @DisplayName("회원을 조회한다.")
            void 회원_조회() {
                // given
                MemberRequest 스미스_회원_생성_요청 = MemberRequest.createOf(스미스.이메일, 스미스.비밀번호, 스미스.나이, Type.NORMAL, Status.ACTIVE);

                // when
                MemberResponse 스미스_회원_정보 = memberService.createMember(스미스_회원_생성_요청);

                // then
                assertThat(memberService.findMemberById(스미스_회원_정보.getId()))
                        .usingRecursiveComparison()
                        .isEqualTo(스미스_회원_정보);
            }
        }

        @Nested
        class 실패 {
            /**
             * When  생성되지 않은 회원의 번호로 회원을 조회할 경우
             * Then  회원을 조회할 수 없다.
             */
            @Test
            @DisplayName("회원을 조회할 수 없다.")
            void 회원_조회() {
                // given
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            memberService.findMemberById(99L);
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_MEMBER.getMessage());
            }
        }

    }

    @Nested
    class 회원_수정 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성한다.
             * When  회원을 수정한다.
             * Then  회원이 수정된다.
             */
            @Test
            @DisplayName("회원을 수정한다.")
            void 회원_수정() {
                // given
                MemberRequest 스미스_회원_생성_요청 = MemberRequest.createOf(스미스.이메일, 스미스.비밀번호, 스미스.나이, Type.NORMAL, Status.ACTIVE);
                MemberResponse 스미스_회원_정보 = memberService.createMember(스미스_회원_생성_요청);

                // when
                String 변경할_비밀번호 = "New" + 스미스.비밀번호;
                int 변경할_나이 = 스미스.나이 + 10;

                memberService.updateMember(스미스_회원_정보.getId(), MemberRequest.updateOf(변경할_비밀번호, 변경할_나이));

                // then
                Member 회원_조회_정보 = memberRepository.findById(스미스_회원_정보.getId()).get();

                assertThat(회원_조회_정보.getPassword()).isEqualTo(변경할_비밀번호);
                assertThat(회원_조회_정보.getAge()).isEqualTo(변경할_나이);
            }

        }
    }

    @Nested
    class 회원_삭제 {

        @Nested
        class 성공 {

            /**
             * Given 회원을 생성한다.
             * When  회원을 삭제한다.
             * Then  회원의 상태가 '삭제' 상태로 변경된다.
             */
            @Test
            @DisplayName("회원을 삭제한다.")
            void 회원_수정() {
                // given
                MemberRequest 스미스_회원_생성_요청 = MemberRequest.createOf(스미스.이메일, 스미스.비밀번호, 스미스.나이, Type.NORMAL, Status.ACTIVE);
                MemberResponse 스미스_회원_정보 = memberService.createMember(스미스_회원_생성_요청);

                // when
                memberService.deleteMember(스미스_회원_정보.getId());

                // then
                Member 스미스_회원_상태 = memberRepository.findById(스미스_회원_정보.getId()).get();

                assertThat(스미스_회원_상태.getStatus()).isEqualTo(Status.WITHDRAWN);
            }

        }
    }

}
