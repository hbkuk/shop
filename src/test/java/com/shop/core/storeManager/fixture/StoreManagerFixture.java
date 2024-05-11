package com.shop.core.storeManager.fixture;

public enum StoreManagerFixture {
    김상점("kim@store.com", "kim_password", "010-1234-5678"),
    박상점("park@store.com", "park_password", "010-2345-6789"),
    이상점("lee@store.com", "lee_password", "010-3456-7890"),
    최상점("choi@store.com", "choi_password", "010-4567-8901"),
    정상점("jung@store.com", "jung_password", "010-5678-9012");

    public final String 이메일;
    public final String 비밀번호;
    public final String 핸드폰_번호;

    StoreManagerFixture(String 이메일, String 비밀번호, String 핸드폰_번호) {
        this.이메일 = 이메일;
        this.비밀번호 = 비밀번호;
        this.핸드폰_번호 = 핸드폰_번호;
    }

    public String get이메일() {
        return 이메일;
    }

    public String get비밀번호() {
        return 비밀번호;
    }

    public String get핸드폰_번호() {
        return 핸드폰_번호;
    }
}

