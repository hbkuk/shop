package com.shop.core.memberSecurity.domain;

import com.shop.core.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberSecurityRepository extends JpaRepository<MemberSecurity, Long> {

    @Query("SELECT s FROM MemberSecurity s WHERE s.member = :member")
    MemberSecurity findByMember(@Param("member") Member member);
}
