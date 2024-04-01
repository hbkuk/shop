package com.shop.common.util;

import com.shop.core.admin.auth.domain.Admin;
import com.shop.core.admin.auth.domain.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminAcceptanceTest extends AcceptanceTest{

    @Autowired
    AdminRepository adminRepository;

    public void 관리자_등록(Admin 관리자_정보) {
        adminRepository.save(관리자_정보);
    }
}
