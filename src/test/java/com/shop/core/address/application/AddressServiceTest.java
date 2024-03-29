package com.shop.core.address.application;

import com.shop.common.annotation.ApplicationTest;
import com.shop.common.exception.ErrorType;
import com.shop.core.address.domain.Address;
import com.shop.core.address.domain.AddressRepository;
import com.shop.core.address.exception.NotFoundAddressException;
import com.shop.core.address.presentation.dto.AddressRequest;
import com.shop.core.address.presentation.dto.AddressResponse;
import com.shop.core.auth.domain.LoginUser;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.application.dto.MemberResponse;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.Status;
import com.shop.core.member.domain.Type;
import com.shop.core.member.exception.NonMatchingMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.member.fixture.MemberFixture.존슨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ApplicationTest
@DisplayName("주소록 서비스 레이어 테스트")
public class AddressServiceTest {

    @Autowired
    AddressService addressService;

    @Autowired
    MemberService memberService;

    @Autowired
    AddressRepository addressRepository;

    @Nested
    class 주소록_등록 {

        @Nested
        class 성공 {

            @Test
            @DisplayName("주소록을 등록할 수 있다.")
            void 주소록_등록_성공() {
                // given
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);

                // when
                AddressRequest 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                AddressResponse 주소록_등록_정보 = addressService.create(주소록_등록_요청_정보, LoginUser.of(생성된_회원.getEmail()));

                // then
                Address 찾은_주소록_정보 = addressRepository.findById(주소록_등록_정보.getId()).get();
                assertThat(AddressResponse.of(찾은_주소록_정보)).usingRecursiveComparison().isEqualTo(주소록_등록_정보);
            }
        }
    }

    @Nested
    class 주소록_찾기 {

        @Nested
        class 성공 {

            @Test
            @DisplayName("주소록을 찾을 수 있다.")
            void 주소록_찾기_성공() {
                // given
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);

                AddressRequest 등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(등록할_주소록_정보.toEntity(생성된_회원.getId()));

                // when
                AddressResponse 찾은_주소록 = addressService.findById(저장된_주소록.getId(), LoginUser.of(생성된_회원.getEmail()));

                // then
                assertThat(등록할_주소록_정보).usingRecursiveComparison()
                        .ignoringFields("id").isEqualTo(찾은_주소록);
            }
        }

        @Nested
        class 실패 {

            @Test
            @DisplayName("타인의 주소록을 찾을 수 없다.")
            void 타인의_주소록_찾기() {
                // given
                Member 스미스_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);
                Member 존슨_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이);

                AddressRequest 등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(등록할_주소록_정보.toEntity(스미스_회원.getId()));

                // when,then
                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            addressService.findById(저장된_주소록.getId(), LoginUser.of(존슨_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_MEMBER.getMessage());
            }


            @Test
            @DisplayName("등록되지 않은 주소록을 찾을 수 없다.")
            void 존재하지_않는_주소록_찾기() {
                // given
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);

                Long 존재하지_않는_주소록_번호 = 100L;

                // when,then
                assertThatExceptionOfType(NotFoundAddressException.class)
                        .isThrownBy(() -> {
                            addressService.findById(존재하지_않는_주소록_번호, LoginUser.of(생성된_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_ADDRESS.getMessage());
            }
        }
    }

    @Nested
    class 주소록_변경 {

        @Nested
        class 성공 {

            @Test
            @DisplayName("주소록을 변경할 수 있다.")
            void 주소록_변경_성공() {
                // given
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);

                AddressRequest 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(주소록_등록_요청_정보.toEntity(생성된_회원.getId()));

                // when
                AddressRequest 주소록_수정_요청_정보 = AddressRequest.of("서울특별시 강남구 대치동 435-12", "대치타워빌딩 5층 1001호", "학원 주소", true);
                AddressResponse 주소록_수정_정보 = addressService.update(AddressRequest.mergeAddressId(저장된_주소록.getId(), 주소록_수정_요청_정보), LoginUser.of(생성된_회원.getEmail()));

                // then
                Address 찾은_주소록_정보 = addressRepository.findById(저장된_주소록.getId()).get();
                assertThat(AddressResponse.of(찾은_주소록_정보)).usingRecursiveComparison().isEqualTo(주소록_수정_정보);
            }
        }

        @Nested
        class 실패 {

            @Test
            @DisplayName("타인의 주소록을 찾을 수 없다.")
            void 타인의_주소록_변경() {
                // given
                Member 스미스_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);
                Member 존슨_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이);

                AddressRequest 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(주소록_등록_요청_정보.toEntity(스미스_회원.getId()));

                // when,then
                AddressRequest 주소록_수정_요청_정보 = AddressRequest.of("서울특별시 강남구 대치동 435-12", "대치타워빌딩 5층 1001호", "학원 주소", true);

                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            AddressResponse 주소록_수정_정보 = addressService.update(AddressRequest.mergeAddressId(저장된_주소록.getId(), 주소록_수정_요청_정보), LoginUser.of(존슨_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_MEMBER.getMessage());
            }
        }
    }

    @Nested
    class 기본_주소록_변경 {

        @Nested
        class 성공 {

            @Test
            @DisplayName("기본 주소록을 변경할 수 있다.")
            void 기본_주소록_변경_성공() {
                // given
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);

                addressRepository.save(AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", false).toEntity(생성된_회원.getId()));
                Address 두번째_주소록 = addressRepository.save(AddressRequest.of("대전광역시 서구 둔산동 123-456", "둔산한화꿈에그린 10층 1001호", "집 주소", false).toEntity(생성된_회원.getId()));
                addressRepository.save(AddressRequest.of("경기도 수원시 장안구 영화동 789-012", "영화아파트 101동 201호", "집 주소", true).toEntity(생성된_회원.getId()));

                // when
                addressService.updateDefaultAddress(두번째_주소록.getId(), LoginUser.of(생성된_회원.getEmail()));

                // then
                assertThat(addressRepository.findDefaultAddressByMemberId(생성된_회원.getId()))
                        .usingRecursiveComparison().comparingOnlyFields("id")
                        .isEqualTo(두번째_주소록);
            }

        }

        @Nested
        class 실패 {

            @Test
            @DisplayName("타인의 주소록을 기본 주소록으로 변경할 수 없다.")
            void 기본_주소록_변경_성공() {
                // given
                Member 스미스_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이);
                Member 존슨_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이);

                addressRepository.save(AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", false).toEntity(스미스_회원.getId()));
                Address 두번째_주소록 = addressRepository.save(AddressRequest.of("대전광역시 서구 둔산동 123-456", "둔산한화꿈에그린 10층 1001호", "집 주소", false).toEntity(스미스_회원.getId()));
                addressRepository.save(AddressRequest.of("경기도 수원시 장안구 영화동 789-012", "영화아파트 101동 201호", "집 주소", true).toEntity(스미스_회원.getId()));

                // when
                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            addressService.updateDefaultAddress(두번째_주소록.getId(), LoginUser.of(존슨_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_MEMBER.getMessage());
            }
        }

    }


    private Member 회원_생성(String email, String password, int age) {
        Member member = new Member(email, password, age, Type.NORMAL, Status.ACTIVE);
        MemberResponse memberResponse = memberService.createMember(new MemberRequest(email, password, age, Type.NORMAL, Status.ACTIVE));

        ReflectionTestUtils.setField(member, "id", memberResponse.getId());
        return member;
    }
}
