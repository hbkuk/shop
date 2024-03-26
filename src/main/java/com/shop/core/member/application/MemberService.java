package com.shop.core.member.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.member.application.dto.MemberRequest;
import com.shop.core.member.application.dto.MemberResponse;
import com.shop.core.member.domain.Member;
import com.shop.core.member.domain.MemberRepository;
import com.shop.core.member.domain.Status;
import com.shop.core.member.domain.Type;
import com.shop.core.member.exception.DuplicateEmailException;
import com.shop.core.member.exception.NotFoundMemberException;
import com.shop.core.userSecurity.application.UserSecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserSecurityService userSecurityService;

    public MemberService(MemberRepository memberRepository, UserSecurityService userSecurityService) {
        this.memberRepository = memberRepository;
        this.userSecurityService = userSecurityService;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (isEmailAlreadyRegistered(request.getEmail())) {
            throw new DuplicateEmailException(ErrorType.DUPLICATE_MEMBER_EMAIL);
        }

        Member savedMember = memberRepository.save(request.toMember(Type.NORMAL, Status.ACTIVE));
        userSecurityService.applyPasswordSecurity(savedMember);

        return MemberResponse.of(savedMember);
    }

    public MemberResponse findMemberById(Long id) {
        return MemberResponse.of(findById(id));
    }

    @Transactional
    public void updateMember(Long id, MemberRequest request) {
        Member member = findById(id);
        member.update(request.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        findById(id).updateStatus(Status.WITHDRAWN);
    }

    private Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));
    }

    private boolean isEmailAlreadyRegistered(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}