package com.shop.core.userSecurity.application;

import com.shop.common.security.PasswordSecurityManager;
import com.shop.core.member.domain.Member;
import com.shop.core.userSecurity.domain.UserSecurity;
import com.shop.core.userSecurity.domain.UserSecurityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shop.core.userSecurity.util.UserSecurityUtil.createSalt;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserSecurityService {
    private final UserSecurityRepository userSecurityRepository;
    private final PasswordSecurityManager passwordSecurityManager;

    @Transactional
    public void applyPasswordSecurity(Member member) {
        UserSecurity userSecurity = new UserSecurity(createSalt(), member);

        String encryptedPassword = passwordSecurityManager.encryptedPassword(member.getPassword(), userSecurity.getSalt());
        member.updateEncodedPassword(encryptedPassword);

        userSecurityRepository.save(userSecurity);
    }

    public boolean verifyPassword(String password, Member member) {
        UserSecurity userSecurity = userSecurityRepository.findByMember(member);
        return passwordSecurityManager.isPasswordMatchWithSalt(password, userSecurity.getSalt(), member.getPassword());
    }
}
