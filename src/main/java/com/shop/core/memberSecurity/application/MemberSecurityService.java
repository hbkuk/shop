package com.shop.core.memberSecurity.application;

import com.shop.common.security.PasswordSecurityManager;
import com.shop.core.member.domain.Member;
import com.shop.core.memberSecurity.domain.MemberSecurity;
import com.shop.core.memberSecurity.domain.MemberSecurityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shop.common.util.SecurityUtil.createSalt;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MemberSecurityService {
    private final MemberSecurityRepository memberSecurityRepository;
    private final PasswordSecurityManager passwordSecurityManager;

    @Transactional
    public void applyPasswordSecurity(Member member) {
        MemberSecurity memberSecurity = new MemberSecurity(createSalt(), member);

        String encryptedPassword = passwordSecurityManager.encryptedPassword(member.getPassword(), memberSecurity.getSalt());
        member.updateEncodedPassword(encryptedPassword);

        memberSecurityRepository.save(memberSecurity);
    }

    public boolean verifyPassword(String password, Member member) {
        MemberSecurity memberSecurity = memberSecurityRepository.findByMember(member);
        return passwordSecurityManager.isPasswordMatchWithSalt(password, memberSecurity.getSalt(), member.getPassword());
    }
}
