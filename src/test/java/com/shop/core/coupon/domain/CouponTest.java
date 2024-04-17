package com.shop.core.coupon.domain;

import com.shop.common.exception.ErrorType;
import com.shop.core.coupon.exception.CouponExhaustedException;
import com.shop.core.coupon.exception.CouponIssuanceNotAllowedException;
import com.shop.core.coupon.exception.CouponStatusChangeNotAllowedException;
import com.shop.core.issuedCoupon.domain.IssuedCoupon;
import com.shop.core.issuedCoupon.domain.IssuedCouponStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CouponTest {

    @Nested
    class 쿠폰_발급 {

        @Nested
        class 성공 {

            @Test
            @DisplayName("잔여 발급 횟수가 남아있을 경우 쿠폰을 발급할 수 있다.")
            void 잔여_발급_횟수_남아있음() {
                Long adminId = 1L;
                Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.ISSUABLE, adminId);

                String memberEmail = "member001@gmail.com";
                IssuedCoupon issuedCoupon = new IssuedCoupon(memberEmail, LocalDateTime.now(), LocalDateTime.now(), IssuedCouponStatus.ACTIVE, coupon);

                coupon.issueCoupons(List.of(issuedCoupon));

                assertThat(coupon.getRemainingIssueCount()).isEqualTo(0);
                assertThat(coupon.getIssuedCoupons()).containsExactly(issuedCoupon);
            }

            @Test
            @DisplayName("발급 가능한 상태일 경우 쿠폰을 발급할 수 있다.")
            void 발급_가능한_상태() {
                Long adminId = 1L;
                Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.ISSUABLE, adminId);

                String memberEmail = "member001@gmail.com";
                IssuedCoupon issuedCoupon = new IssuedCoupon(memberEmail, LocalDateTime.now(), LocalDateTime.now(), IssuedCouponStatus.ACTIVE, coupon);

                coupon.issueCoupons(List.of(issuedCoupon));

                assertThat(coupon.getRemainingIssueCount()).isEqualTo(0);
                assertThat(coupon.getIssuedCoupons()).containsExactly(issuedCoupon);
            }
        }

        @Nested
        class 실패 {

            @Test
            @DisplayName("잔여 발급 횟수가 남아있지 않는 경우 쿠폰을 발급할 수 없다.")
            void 잔여_발급_횟수_남지_않음() {
                Long adminId = 1L;
                Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 0, LocalDateTime.now(), CouponStatus.ISSUABLE, adminId);

                String memberEmail = "member001@gmail.com";
                IssuedCoupon issuedCoupon = new IssuedCoupon(memberEmail, LocalDateTime.now(), LocalDateTime.now(), IssuedCouponStatus.ACTIVE, coupon);

                assertThatExceptionOfType(CouponExhaustedException.class)
                        .isThrownBy(() -> {
                            coupon.issueCoupons(List.of(issuedCoupon));
                        })
                        .withMessageMatching(ErrorType.COUPON_EXHAUSTED.getMessage());
            }

            @Test
            @DisplayName("발급 중지 상태에서 쿠폰을 발급할 수 없다.")
            void 발급_중지_상태에서_쿠폰_발급() {
                Long adminId = 1L;
                Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.STOPPED_ISSUANCE, adminId);

                String memberEmail = "member001@gmail.com";
                IssuedCoupon issuedCoupon = new IssuedCoupon(memberEmail, LocalDateTime.now(), LocalDateTime.now(), IssuedCouponStatus.ACTIVE, coupon);

                assertThatExceptionOfType(CouponIssuanceNotAllowedException.class)
                        .isThrownBy(() -> {
                            coupon.issueCoupons(List.of(issuedCoupon));
                        })
                        .withMessageMatching(ErrorType.COUPON_ISSUANCE_NOT_ALLOWED.getMessage());
            }

            @Test
            @DisplayName("삭제 상태에서 쿠폰을 발급할 수 없다.")
            void 삭제_상태에서_쿠폰_발급() {
                Long adminId = 1L;
                Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.DELETED, adminId);

                String memberEmail = "member001@gmail.com";
                IssuedCoupon issuedCoupon = new IssuedCoupon(memberEmail, LocalDateTime.now(), LocalDateTime.now(), IssuedCouponStatus.ACTIVE, coupon);

                assertThatExceptionOfType(CouponIssuanceNotAllowedException.class)
                        .isThrownBy(() -> {
                            coupon.issueCoupons(List.of(issuedCoupon));
                        })
                        .withMessageMatching(ErrorType.COUPON_ISSUANCE_NOT_ALLOWED.getMessage());
            }

            @Test
            @DisplayName("소진 상태에서 쿠폰을 발급할 수 없다.")
            void 소진_상태에서_쿠폰_발급() {
                Long adminId = 1L;
                Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 0, LocalDateTime.now(), CouponStatus.EXHAUSTED, adminId);

                String memberEmail = "member001@gmail.com";
                IssuedCoupon issuedCoupon = new IssuedCoupon(memberEmail, LocalDateTime.now(), LocalDateTime.now(), IssuedCouponStatus.ACTIVE, coupon);

                assertThatExceptionOfType(CouponExhaustedException.class)
                        .isThrownBy(() -> {
                            coupon.issueCoupons(List.of(issuedCoupon));
                        })
                        .withMessageMatching(ErrorType.COUPON_EXHAUSTED.getMessage());
            }
        }
    }

    @Nested
    class 쿠폰_상태_변경 {

        @Nested
        class 성공 {

            @Nested
            class 발급_가능_상태 {

                @Test
                @DisplayName("발급 가능 상태에서 발급 중지 상태로 변경할 수 있다.")
                void 발급_중지_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.ISSUABLE, adminId);

                    Coupon updatedCoupon = coupon.updateStatus(CouponStatus.STOPPED_ISSUANCE);

                    assertThat(updatedCoupon.getCouponStatus()).isEqualTo(CouponStatus.STOPPED_ISSUANCE);
                }

                @Test
                @DisplayName("발급 가능 상태에서 삭제 상태로 변경할 수 있다.")
                void 삭제_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.ISSUABLE, adminId);

                    Coupon updatedCoupon = coupon.updateStatus(CouponStatus.DELETED);

                    assertThat(updatedCoupon.getCouponStatus()).isEqualTo(CouponStatus.DELETED);
                }
            }

            @Nested
            class 발급_중지_상태 {

                @Test
                @DisplayName("발급 중지 상태에서 발급 가능 상태로 변경할 수 있다.")
                void 발급_가능_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.STOPPED_ISSUANCE, adminId);

                    Coupon updatedCoupon = coupon.updateStatus(CouponStatus.ISSUABLE);

                    assertThat(updatedCoupon.getCouponStatus()).isEqualTo(CouponStatus.ISSUABLE);
                }

                @Test
                @DisplayName("발급 중지 상태에서 삭제 상태로 변경할 수 있다.")
                void 삭제_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.STOPPED_ISSUANCE, adminId);

                    Coupon updatedCoupon = coupon.updateStatus(CouponStatus.DELETED);

                    assertThat(updatedCoupon.getCouponStatus()).isEqualTo(CouponStatus.DELETED);
                }
            }
        }

        @Nested
        class 실패 {

            @Nested
            class 발급_가능_상태 {

                @Test
                @DisplayName("발급 가능 상태에서 소진 상태로 변경할 수 없다.")
                void 소진_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.ISSUABLE, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.EXHAUSTED);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }
            }

            @Nested
            class 발급_중지_상태 {

                @Test
                @DisplayName("발급 중지 상태에서 소진 상태로 변경할 수 없다.")
                void 소진_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.STOPPED_ISSUANCE, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.EXHAUSTED);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }
            }

            @Nested
            class 삭제_상태 {

                @Test
                @DisplayName("삭제 상태에서 발급 가능 상태로 변경할 수 없다.")
                void 발급_가능_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.DELETED, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.ISSUABLE);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }

                @Test
                @DisplayName("삭제 상태에서 발급 중지 상태로 변경할 수 없다.")
                void 발급_중지_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.DELETED, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.STOPPED_ISSUANCE);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }

                @Test
                @DisplayName("삭제 상태에서 소진 상태로 변경할 수 없다.")
                void 소진_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.DELETED, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.EXHAUSTED);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }
            }

            @Nested
            class 소진_상태 {

                @Test
                @DisplayName("소진 상태에서 발급 가능 상태로 변경할 수 없다.")
                void 발급_가능_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.EXHAUSTED, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.ISSUABLE);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }

                @Test
                @DisplayName("소진 상태에서 발급 중지 상태로 변경할 수 없다.")
                void 발급_중지_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.EXHAUSTED, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.STOPPED_ISSUANCE);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }

                @Test
                @DisplayName("소진 상태에서 삭제 상태로 변경할 수 없다.")
                void 삭제_상태로_변경() {
                    Long adminId = 1L;
                    Coupon coupon = new Coupon("봄 맞이 특별 쿠폰", "인기 브랜드의 다양한 제품 할인", 30000, 10, 1, LocalDateTime.now(), CouponStatus.EXHAUSTED, adminId);

                    assertThatExceptionOfType(CouponStatusChangeNotAllowedException.class)
                            .isThrownBy(() -> {
                                coupon.updateStatus(CouponStatus.DELETED);
                            })
                            .withMessageMatching(ErrorType.COUPON_STATUS_CHANGE_NOT_ALLOWED.getMessage());
                }
            }
        }
    }
}
