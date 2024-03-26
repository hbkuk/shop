package com.shop.core.userSecurity.domain;

import com.shop.core.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {

    @Query("SELECT s FROM UserSecurity s WHERE s.member = :member")
    UserSecurity findByMember(@Param("member") Member member);
}
