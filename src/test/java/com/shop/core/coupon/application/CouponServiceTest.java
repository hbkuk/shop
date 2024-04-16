package com.shop.core.coupon.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.admin.auth.application.AdminAuthService;
import com.shop.core.admin.auth.domain.Admin;
import com.shop.core.admin.auth.domain.AdminRole;
import com.shop.core.admin.auth.domain.AdminSignupChannel;
import com.shop.core.admin.auth.domain.AdminStatus;
import com.shop.core.admin.auth.exception.NotFoundAdminException;
import com.shop.core.auth.domain.LoginUser;
import com.shop.core.coupon.domain.Coupon;
import com.shop.core.coupon.domain.CouponRepository;
import com.shop.core.coupon.domain.CouponStatus;
import com.shop.core.coupon.exception.CouponExhaustedException;
import com.shop.core.coupon.exception.NotFoundCouponException;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.coupon.presentation.dto.CouponStatusRequest;
import com.shop.core.issuedCoupon.domain.IssuedCoupon;
import com.shop.core.issuedCoupon.domain.IssuedCouponRepository;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.shop.core.admin.auth.fixture.AdminGithubFixture.황병국;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.member.fixture.MemberFixture.존슨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("쿠폰 서비스 레이어 테스트")
public class CouponServiceTest extends ApplicationTest {

    @Autowired
    CouponService couponService;

    @Autowired
    AdminAuthService adminAuthService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    IssuedCouponRepository issuedCouponRepository;

    @Nested
    class 동시에_쿠폰_100개_발급 {

        Admin 관리자;

        Coupon 쿠폰;

        int 초기_잔여_발급_쿠폰_개수;

        @BeforeEach
        void 사전_준비() {
            관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

            랜덤_회원_목록_생성(10, MemberType.NORMAL, MemberStatus.ACTIVE);

            초기_잔여_발급_쿠폰_개수 = 3;
            CouponRequest 등록할_쿠폰_정보 =
                    CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 초기_잔여_발급_쿠폰_개수);
            쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 관리자.getId()));
        }

        @Nested
        class 성공 {

            @Test
            @DisplayName("동시다발적으로 쿠폰을 발급한다.")
            void 동시에_쿠폰_발급_성공() throws InterruptedException {
                // given
                동시_쿠폰_발급(10);

                // when
                int 발급된_쿠폰_개수 = couponRepository.findCouponWithIssuedCoupons(쿠폰.getId()).getIssuedCoupons().size();

                // then
                assertThat(초기_잔여_발급_쿠폰_개수).isEqualTo(발급된_쿠폰_개수);
            }

            private void 동시_쿠폰_발급(int threadCount) throws InterruptedException {
                ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                CountDownLatch latch = new CountDownLatch(threadCount);

                for (int i = 0; i < threadCount; i++) {
                    Long index = (long) i;
                    executorService.submit(() -> {
                        try {
                            couponService.issueCoupon(CouponIssueRequest.of(쿠폰.getId(), List.of(index)), LoginUser.of(관리자.getEmail()));
                        } finally {
                            latch.countDown();
                        }
                    });
                }
                latch.await();
            }

        }
    }

    @Nested
    class 쿠폰_발급 {

        @Nested
        class 성공 {

            @Nested
            class 대량_쿠폰_발급 {

                List<Member> 생성된_회원_목록;
                Admin 생성된_관리자;

                Coupon 등록된_쿠폰;

                @BeforeEach
                void 사전_준비() {
                    생성된_회원_목록 = 랜덤_회원_목록_생성(100, MemberType.NORMAL, MemberStatus.ACTIVE);
                    생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

                    CouponRequest 등록할_쿠폰_정보 =
                            CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                    등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));
                }

                @Test
                @DisplayName("정상적으로 쿠폰을 발급한다.")
                void 쿠폰_정상_발급() {
                    // given
                    List<Long> 회원_번호_목록 = 생성된_회원_목록.stream().map(Member::getId).collect(Collectors.toList());

                    // when
                    CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), 회원_번호_목록);
                    couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));

                    // then
                    List<IssuedCoupon> 발급된_쿠폰_목록 = issuedCouponRepository.findByCoupon(등록된_쿠폰);
                    List<Long> 발급된_쿠폰_목록의_회원_번호 = 발급된_쿠폰_목록.stream().map(IssuedCoupon::getMemberId).collect(Collectors.toList());

                    assertThat(발급된_쿠폰_목록의_회원_번호).isEqualTo(회원_번호_목록);
                }

            }

            @Nested
            class 소량_쿠폰_발급 {

                Member 생성된_회원;
                Admin 생성된_관리자;

                Coupon 등록된_쿠폰;

                @BeforeEach
                void 사전_준비() {
                    생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                    생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

                    CouponRequest 등록할_쿠폰_정보 =
                            CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                    등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));
                }

                @Test
                @DisplayName("정상적으로 쿠폰을 발급한다.")
                void 쿠폰_정상_발급() {
                    // when
                    CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(생성된_회원.getId()));
                    couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));

                    // then
                    List<IssuedCoupon> 발급된_쿠폰_목록 = issuedCouponRepository.findByCoupon(등록된_쿠폰);
                    List<Long> 발급된_쿠폰_목록의_회원_번호 = 발급된_쿠폰_목록.stream().map(IssuedCoupon::getMemberId).collect(Collectors.toList());

                    assertThat(발급된_쿠폰_목록의_회원_번호).containsExactlyInAnyOrder(생성된_회원.getId());
                }
            }
        }

        @Nested
        class 실패 {

            Member 첫번째_생성된_회원;
            Member 두번째_생성된_회원;

            Admin 생성된_관리자;

            Long 존재하지_않는_회원_번호 = 100L;
            Long 존재하지_않는_관리자_번호 = 100L;

            String 존재하지_않는_관리자_이메일 = "admin001@email.com";

            @BeforeEach
            void 사전_준비() {
                첫번째_생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                두번째_생성된_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);
            }

            @Test
            @DisplayName("잔여 발급 횟수가 남지 않았을 경우 쿠폰을 발급할 수 없다.")
            void 잔여_발급_횟수_남지_않음() {
                // given
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));

                // when, then
                CouponIssueRequest 발급할_첫번째_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(첫번째_생성된_회원.getId()));
                couponService.issueCoupon(발급할_첫번째_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));

                CouponIssueRequest 발급할_두번째_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(두번째_생성된_회원.getId()));
                assertThatExceptionOfType(CouponExhaustedException.class)
                        .isThrownBy(() -> {
                            couponService.issueCoupon(발급할_두번째_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));
                        })
                        .withMessageMatching(ErrorType.COUPON_EXHAUSTED.getMessage());
            }

            @Test
            @DisplayName("존재하지 않는 회원에게 쿠폰을 발급할 수 없다.")
            void 존재하지_않는_회원에게_발급() {
                // when, then
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));

                CouponIssueRequest 발급할_첫번째_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(존재하지_않는_회원_번호));
                assertThatExceptionOfType(NotFoundMemberException.class)
                        .isThrownBy(() -> {
                            couponService.issueCoupon(발급할_첫번째_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_MEMBER.getMessage());
            }

            @Test
            @DisplayName("존재하지 않는 관리자는 쿠폰을 발급할 수 없다.")
            void 존재하지_않는_관리자가_발급() {
                // when
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 존재하지_않는_관리자_번호));

                CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(첫번째_생성된_회원.getId()));
                assertThatExceptionOfType(NotFoundAdminException.class)
                        .isThrownBy(() -> {
                            couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(존재하지_않는_관리자_이메일));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_ADMIN.getMessage());
            }
        }
    }

    @Nested
    class 쿠폰_상태_변경 {

        Member 첫번째_생성된_회원;
        Member 두번째_생성된_회원;

        Admin 생성된_관리자;

        Long 존재하지_않는_회원_번호 = 100L;
        Long 존재하지_않는_관리자_번호 = 100L;

        String 존재하지_않는_관리자_이메일 = "admin001@email.com";

        Long 존재하지_않는_쿠폰_정보 = 100L;

        @BeforeEach
        void 사전_준비() {
            첫번째_생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            두번째_생성된_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

            생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);
        }

        @Nested
        class 성공 {

            @Test
            @DisplayName("정상적으로 쿠폰 상태가 변경된다.")
            void 쿠폰_상태_변경_성공() {
                // given
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));

                // when
                CouponStatusRequest 쿠폰_상태_변경_정보 = CouponStatusRequest.of(등록된_쿠폰.getId(), CouponStatus.DELETED);
                couponService.updateStatus(쿠폰_상태_변경_정보, LoginUser.of(생성된_관리자.getEmail()));

                // then
                Coupon 조회한_쿠폰 = couponRepository.findById(등록된_쿠폰.getId()).get();
                assertThat(조회한_쿠폰.getCouponStatus()).isEqualTo(CouponStatus.DELETED);
            }
        }

        @Nested
        class 실패 {

            @Test
            @DisplayName("존재하지 않는 관리자는 쿠폰 상태를 변경할 수 없다.")
            void 존재하지_않는_관리자가_상태_변경() {
                // given
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 100);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 존재하지_않는_관리자_번호));

                // when, then
                CouponStatusRequest 쿠폰_상태_변경_정보 = CouponStatusRequest.of(등록된_쿠폰.getId(), CouponStatus.DELETED);
                assertThatExceptionOfType(NotFoundAdminException.class)
                        .isThrownBy(() -> {
                            couponService.updateStatus(쿠폰_상태_변경_정보, LoginUser.of(존재하지_않는_관리자_이메일));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_ADMIN.getMessage());
            }

            @Test
            @DisplayName("존재하지 않는 쿠폰의 상태를 변경할 수 없다.")
            void 존재하지_않는_쿠폰_상태_변경() {
                // when, then
                CouponStatusRequest 쿠폰_상태_변경_정보 = CouponStatusRequest.of(존재하지_않는_쿠폰_정보, CouponStatus.DELETED);
                assertThatExceptionOfType(NotFoundCouponException.class)
                        .isThrownBy(() -> {
                            couponService.updateStatus(쿠폰_상태_변경_정보, LoginUser.of(생성된_관리자.getEmail()));
                        })
                        .withMessageMatching(ErrorType.NOT_FOUND_COUPON.getMessage());
            }
        }
    }

    @Nested
    class 쿠폰_소진_상태_자동_변경 {

        Member 첫번째_생성된_회원;
        Member 두번째_생성된_회원;

        Admin 생성된_관리자;

        @BeforeEach
        void 사전_준비() {
            첫번째_생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
            두번째_생성된_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

            생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);
        }

        @Nested
        class 변경됨 {

            @Test
            @DisplayName("잔여 발급 횟수가 0으로 변경될 경우, 소진 상태로 저장된다.")
            void 소진_상태_자동_변경() {
                // given
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));

                CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(첫번째_생성된_회원.getId()));
                couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));

                // then
                Coupon 찾은_쿠폰 = couponRepository.findById(등록된_쿠폰.getId()).get();

                assertThat(찾은_쿠폰.getCouponStatus()).isEqualTo(CouponStatus.EXHAUSTED);
                assertThat(찾은_쿠폰.getRemainingIssueCount()).isEqualTo(0);

            }
        }

        @Nested
        class 변경_안됨 {

            @Test
            @DisplayName("잔여 발급 횟수가 0보다 큰 경우, 소진 상태로 저장되지 않는다.")
            void 소진_상태_변경_안됨() {
                // given
                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 2);
                Coupon 등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(LocalDateTime.now(), CouponStatus.ISSUABLE, 생성된_관리자.getId()));

                // when
                CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(첫번째_생성된_회원.getId()));
                couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));

                // then
                Coupon 찾은_쿠폰 = couponRepository.findById(등록된_쿠폰.getId()).get();

                assertThat(찾은_쿠폰.getCouponStatus()).isEqualTo(CouponStatus.ISSUABLE);
                assertThat(찾은_쿠폰.getRemainingIssueCount()).isEqualTo(1);
            }
        }

    }
}
