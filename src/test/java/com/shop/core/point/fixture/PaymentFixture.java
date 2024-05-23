package com.shop.core.point.fixture;

public enum PaymentFixture {
    포인트_만원_충전("1234567890", 10000);

    public final String 결제_번호;
    public final int 금액;

    PaymentFixture(String 결제_번호, int 금액) {
        this.결제_번호 = 결제_번호;
        this.금액 = 금액;
    }

    public String get결제_번호() {
        return 결제_번호;
    }

    public int get금액() {
        return 금액;
    }
}
