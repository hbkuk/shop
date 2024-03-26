package com.shop.core.member.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.auth.application.UserDetailsService;
import com.shop.core.member.domain.Member;
import com.shop.core.member.exception.PasswordMismatchException;
import com.shop.core.memberSecurity.application.MemberSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    MemberService memberService;
    MemberSecurityService memberSecurityService;

    @Override
    public boolean verifyUser(String email, String password) {
        Member member = memberService.findMemberByEmail(email);

        if (!memberSecurityService.verifyPassword(password, member)) {
            throw new PasswordMismatchException(ErrorType.PASSWORD_MISMATCH);
        }
        return true;
    }
}
