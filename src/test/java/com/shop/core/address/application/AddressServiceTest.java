package com.shop.core.address.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.address.domain.Address;
import com.shop.core.address.domain.AddressRepository;
import com.shop.core.address.exception.NotFoundAddressException;
import com.shop.core.address.presentation.dto.AddressRequest;
import com.shop.core.address.presentation.dto.AddressResponse;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.member.exception.NonMatchingMemberException;
import com.shop.core.memberAuth.domain.LoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.member.fixture.MemberFixture.존슨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("주소록 서비스 레이어 테스트")
public class AddressServiceTest extends ApplicationTest {

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
                회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                // when
                AddressRequest 주소록_등록_요청_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                AddressResponse 등록할_주소록_정보 = addressService.create(주소록_등록_요청_정보, LoginUser.of(스미스.이메일));

                // then
                AddressResponse 찾은_주소록_정보 = AddressResponse.of(addressRepository.findById(등록할_주소록_정보.getId()).get());
                assertThat(찾은_주소록_정보).usingRecursiveComparison().isEqualTo(등록할_주소록_정보);
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
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

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
                Member 생성된_첫번째_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                Member 생성된_두번째_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                AddressRequest 등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(등록할_주소록_정보.toEntity(생성된_첫번째_회원.getId()));

                // when,then
                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            addressService.findById(저장된_주소록.getId(), LoginUser.of(생성된_두번째_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_MEMBER.getMessage());
            }


            @Test
            @DisplayName("등록되지 않은 주소록을 찾을 수 없다.")
            void 존재하지_않는_주소록_찾기() {
                // given
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

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
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                AddressRequest 등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(등록할_주소록_정보.toEntity(생성된_회원.getId()));

                // when
                AddressRequest 수정할_주소록_정보 = AddressRequest.of("서울특별시 강남구 대치동 435-12", "대치타워빌딩 5층 1001호", "학원 주소", true);
                AddressResponse 수정된_주소록_정보 = addressService.update(AddressRequest.mergeAddressId(저장된_주소록.getId(), 수정할_주소록_정보), LoginUser.of(생성된_회원.getEmail()));

                // then
                AddressResponse 찾은_주소록_정보 = AddressResponse.of(addressRepository.findById(저장된_주소록.getId()).get());
                assertThat(찾은_주소록_정보).usingRecursiveComparison().isEqualTo(수정된_주소록_정보);
            }
        }

        @Nested
        class 실패 {

            @Test
            @DisplayName("타인의 주소록을 찾을 수 없다.")
            void 타인의_주소록_변경() {
                // given
                Member 생성된_첫번째_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                Member 생성된_두번째_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                AddressRequest 등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", true);
                Address 저장된_주소록 = addressRepository.save(등록할_주소록_정보.toEntity(생성된_첫번째_회원.getId()));

                // when,then
                AddressRequest 수정할_주소록_정보 = AddressRequest.of("서울특별시 강남구 대치동 435-12", "대치타워빌딩 5층 1001호", "학원 주소", true);

                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            addressService.update(AddressRequest.mergeAddressId(저장된_주소록.getId(), 수정할_주소록_정보), LoginUser.of(생성된_두번째_회원.getEmail()));
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
                Member 생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                AddressRequest 첫번째_등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", false);
                Address 첫번째_주소록 = addressRepository.save(첫번째_등록할_주소록_정보.toEntity(생성된_회원.getId()));

                AddressRequest 두번째_등록할_주소록_정보 = AddressRequest.of("대전광역시 서구 둔산동 123-456", "둔산한화꿈에그린 10층 1001호", "집 주소", false);
                Address 두번째_주소록 = addressRepository.save(두번째_등록할_주소록_정보.toEntity(생성된_회원.getId()));

                AddressRequest 세번째_등록할_주소록_정보 = AddressRequest.of("경기도 수원시 장안구 영화동 789-012", "영화아파트 101동 201호", "집 주소", true);
                Address 세번째_주소록 = addressRepository.save(세번째_등록할_주소록_정보.toEntity(생성된_회원.getId()));

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
                Member 생성된_첫번째_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                Member 생성된_두번째_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                AddressRequest 첫번째_등록할_주소록_정보 = AddressRequest.of("서울특별시 강남구 역삼동 123-45", "역삼타워빌딩 5층 501호", "회사 주소", false);
                Address 첫번째_주소록 = addressRepository.save(첫번째_등록할_주소록_정보.toEntity(생성된_첫번째_회원.getId()));

                AddressRequest 두번째_등록할_주소록_정보 = AddressRequest.of("대전광역시 서구 둔산동 123-456", "둔산한화꿈에그린 10층 1001호", "집 주소", false);
                Address 두번째_주소록 = addressRepository.save(두번째_등록할_주소록_정보.toEntity(생성된_첫번째_회원.getId()));

                AddressRequest 세번째_등록할_주소록_정보 = AddressRequest.of("경기도 수원시 장안구 영화동 789-012", "영화아파트 101동 201호", "집 주소", true);
                Address 세번째_주소록 = addressRepository.save(세번째_등록할_주소록_정보.toEntity(생성된_첫번째_회원.getId()));

                // when
                assertThatExceptionOfType(NonMatchingMemberException.class)
                        .isThrownBy(() -> {
                            addressService.updateDefaultAddress(두번째_주소록.getId(), LoginUser.of(생성된_두번째_회원.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NON_MATCHING_MEMBER.getMessage());
            }
        }

    }
}
