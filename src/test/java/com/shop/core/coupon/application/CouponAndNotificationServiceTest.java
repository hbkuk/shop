package com.shop.core.coupon.application;

import com.shop.common.exception.ErrorType;
import com.shop.common.util.ApplicationTest;
import com.shop.core.adminAuth.application.AdminAuthService;
import com.shop.core.adminAuth.domain.Admin;
import com.shop.core.adminAuth.domain.AdminRole;
import com.shop.core.adminAuth.domain.AdminSignupChannel;
import com.shop.core.adminAuth.domain.AdminStatus;
import com.shop.core.coupon.domain.Coupon;
import com.shop.core.coupon.domain.CouponRepository;
import com.shop.core.coupon.domain.CouponStatus;
import com.shop.core.coupon.exception.InsufficientCouponQuantityException;
import com.shop.core.coupon.presentation.dto.CouponRequest;
import com.shop.core.issuedCoupon.domain.IssuedCouponRepository;
import com.shop.core.issuedCoupon.presentation.dto.CouponIssueRequest;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.memberAuth.domain.LoginUser;
import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationRepository;
import com.shop.core.notification.domain.NotificationType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static com.shop.core.adminAuth.fixture.AdminGithubFixture.황병국;
import static com.shop.core.member.fixture.MemberFixture.스미스;
import static com.shop.core.member.fixture.MemberFixture.존슨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Disabled
@DisplayName("쿠폰 서비스와 알림 서비스 테스트")
public class CouponAndNotificationServiceTest extends ApplicationTest {

    @Autowired
    CouponService couponService;

    @Autowired
    AdminAuthService adminAuthService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    IssuedCouponRepository issuedCouponRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Nested
    class 쿠폰_발급시_알림_발송 {

        @Nested
        class 성공 {

            Member 첫번째_생성된_회원;
            Member 두번째_생성된_회원;

            Admin 생성된_관리자;

            Coupon 등록된_쿠폰;

            @BeforeEach
            void 사전_준비() {
                첫번째_생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                두번째_생성된_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 2);
                등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(CouponStatus.ISSUABLE, 생성된_관리자.getEmail()));
            }

            @Test
            @DisplayName("쿠폰이 발급된 경우, 알림이 발송된다.")
            void 쿠폰_정상_발급_시_알림_발송() {
                // given
                CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(첫번째_생성된_회원.getEmail(), 두번째_생성된_회원.getEmail()));

                // when, then
                couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));

                assertThat(모든_쿠폰_알림_발송의_회원_이메일_가져오기()).containsExactlyInAnyOrder(첫번째_생성된_회원.getEmail(), 두번째_생성된_회원.getEmail());
            }
        }

        @Nested
        class 실패 {

            Member 첫번째_생성된_회원;
            Member 두번째_생성된_회원;

            Admin 생성된_관리자;

            Coupon 등록된_쿠폰;

            @BeforeEach
            void 사전_준비() {
                첫번째_생성된_회원 = 회원_생성(스미스.이메일, 스미스.비밀번호, 스미스.나이, MemberType.NORMAL, MemberStatus.ACTIVE);
                두번째_생성된_회원 = 회원_생성(존슨.이메일, 존슨.비밀번호, 존슨.나이, MemberType.NORMAL, MemberStatus.ACTIVE);

                생성된_관리자 = 관리자_생성(황병국.email, 황병국.phoneNumber, AdminRole.ADMIN, AdminSignupChannel.GITHUB, AdminStatus.ACTIVE);

                CouponRequest 등록할_쿠폰_정보 =
                        CouponRequest.of("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1);
                등록된_쿠폰 = couponRepository.save(등록할_쿠폰_정보.toEntity(CouponStatus.ISSUABLE, 생성된_관리자.getEmail()));
            }

            @Test
            @DisplayName("쿠폰이 발급에 실패한 경우, 알림이 발송되지 않는다.")
            void 쿠폰_발급_실패() {
                // given
                CouponIssueRequest 발급할_쿠폰_정보 = CouponIssueRequest.of(등록된_쿠폰.getId(), List.of(첫번째_생성된_회원.getEmail(), 두번째_생성된_회원.getEmail()));

                // when, then
                assertThatExceptionOfType(InsufficientCouponQuantityException.class)
                        .isThrownBy(() -> {
                            couponService.issueCoupon(발급할_쿠폰_정보, LoginUser.of(생성된_관리자.getEmail()));
                        })
                        .withMessageMatching(ErrorType.COUPON_INSUFFICIENT.getMessage());

                assertThat(모든_쿠폰_알림_발송의_회원_이메일_가져오기()).isEmpty();
            }
        }
    }

    private List<String> 모든_쿠폰_알림_발송의_회원_이메일_가져오기() {
        List<Notification> 발송된_알림_목록 = notificationRepository.findByNotificationType(NotificationType.COUPON_ISSUANCE);
        return 발송된_알림_목록.stream().map(Notification::getMemberEmail).collect(Collectors.toList());
    }
}
