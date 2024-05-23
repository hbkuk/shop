package com.shop.common.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    INVALID_TOKEN(1000, "유효하지 않은 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(1001, "유효기간이 만료된 액세스 토큰입니다."),

    NOT_FOUND_MEMBER(3001, "존재하지 않는 회원입니다."),
    DUPLICATE_MEMBER_EMAIL(3002, "중복된 회원 이메일입니다."),
    PASSWORD_MISMATCH(3003, "비밀번호가 동일하지 않습니다."),
    NON_MATCHING_SIGNUP_CHANNEL(3005, "회원 가입 경로에 맞는 회원 정보가 없습니다."),

    NOT_FOUND_ADDRESS(4001, "존재하지 않는 주소록입니다."),

    NOT_FOUND_COUPON(5001, "존재하지 않는 쿠폰입니다."),
    COUPON_INSUFFICIENT(5002, "발급 가능한 쿠폰의 수량이 부족합니다."),
    COUPON_EXHAUSTED(5003, "모든 쿠폰이 소진되었습니다."),
    COUPON_STATUS_CHANGE_NOT_ALLOWED(5004, "쿠폰의 상태를 변경할 수 없습니다."),
    COUPON_ISSUANCE_NOT_ALLOWED(5005, "쿠폰을 발급할 수 없는 상태입니다."),
    COUPON_ALREADY_ISSUED(5006, "이미 동일한 쿠폰이 발급되었습니다."),

    NOT_FOUND_NOTIFICATION(6001, "존재하지 않는 알림입니다."),
    CANNOT_NOTIFICATION_READ(6002, "확인할 수 없는 알림입니다."),

    NOT_FOUND_ADMIN(7001, "존재하지 않는 관리자입니다."),
    NOT_FOUND_STORE_MANAGER(7002, "존재하지 않는 상점 관리자입니다."),
    NON_MATCHING_STORE_MANAGER(7002, "다른 상점 관리자의 정보를 확인할 수 없습니다."),

    DUPLICATE_STORE_MANAGER_EMAIL(8001, "중복된 상점 관리자 이메일입니다."),
    DUPLICATE_STORE_NAME(8002, "중복된 상점명 입니다."),
    DUPLICATE_STORE_CREATION(8003, "상점을 생성하는 데 허용된 횟수를 초과했습니다."),
    NOT_FOUND_STORE(8004, "존재하지 않는 상점입니다."),

    DUPLICATE_PRODUCT_NAME(8100, "중복된 상품명입니다."),
    NOT_FOUND_PRODUCT(8101, "존재하지 않는 상품입니다."),
    NON_MATCHING_PRODUCT(8101, "다른 상점의 상품입니다."),


    INVALID_PAYMENT_ID(8201, "잘못된 결제 번호입니다."),


    NON_MATCHING_MEMBER(9000, "다른 회원의 정보를 확인할 수 없습니다."),


    REQUEST_EXCEPTION(9001, "http 요청 에러입니다."),

    INVALID_PATH(9002, "잘못된 경로입니다."),

    UNPROCESSABLE_ENTITY(9003, "요청 데이터가 유효하지 않습니다."),

    DATA_INTEGRITY_VIOLATION(9004, "예상하지 못한 데이터가 입력되었습니다."),

    NO_MORE_TRY(9005, "요청한 작업에 실패했습니다."),

    NOT_FOUND_DATA(9006, "요청한 데이터를 찾을 수 없습니다."),

    UNHANDLED_EXCEPTION(9999, "예상치 못한 예외입니다.");

    private final int code;
    private final String message;

    ErrorType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
