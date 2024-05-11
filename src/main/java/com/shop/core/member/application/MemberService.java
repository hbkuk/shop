package com.shop.core.member.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.application.dto.MemberResponse;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.MemberStatus;
import com.shop.core.member.domain.MemberType;
import com.shop.core.member.exception.DuplicateEmailException;
import com.shop.core.member.exception.NotFoundMemberException;
import com.shop.core.memberSecurity.application.MemberSecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberSecurityService memberSecurityService;

    public MemberService(MemberRepository memberRepository, MemberSecurityService memberSecurityService) {
        this.memberRepository = memberRepository;
        this.memberSecurityService = memberSecurityService;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (isEmailAlreadyRegistered(request.getEmail())) {
            throw new DuplicateEmailException(ErrorType.DUPLICATE_MEMBER_EMAIL);
        }

        Member savedMember = memberRepository.save(request.toEntity(MemberType.NORMAL, MemberStatus.ACTIVE));
        memberSecurityService.applyPasswordSecurity(savedMember);

        return MemberResponse.of(savedMember);
    }

    public MemberResponse findMemberById(Long id) {
        return MemberResponse.of(findById(id));
    }

    @Transactional
    public void updateMember(Long id, MemberRequest request) {
        Member member = findById(id);
        member.update(request.toEntity());
    }

    @Transactional
    public void deleteMember(Long id) {
        findById(id).updateStatus(MemberStatus.WITHDRAWN);
    }

    private Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));
    }

}