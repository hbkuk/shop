package com.shop.core.point.application;

import com.shop.common.domain.auth.LoginUser;
import com.shop.common.exception.ErrorType;
import com.shop.common.exception.NotFoundDataException;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.domain.Member;
import com.shop.core.member.exception.NonMatchingMemberException;
import com.shop.core.point.application.dto.PaymentInfoResponse;
import com.shop.core.point.domain.*;
import com.shop.core.point.presentation.dto.PaymentWebhookRequest;
import com.shop.core.point.presentation.dto.PointResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;

    private final WebhookClient webhookClient;

    private final MemberService memberService;

    @Transactional
    public PointResponse receivePointWebhook(PaymentWebhookRequest request) { // TODO: 리팩토링
        if (request.getPaymentStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException(ErrorType.NO_MORE_TRY.getMessage());
        }

        PaymentInfoResponse paymentInfoResponse = webhookClient.requestPaymentInfo(request.getPaymentId());

        Member member = memberService.findMemberByEmail(paymentInfoResponse.getEmail());
        Point point = new Point(paymentInfoResponse.getAmount(), PointStatus.ACTIVE, PointType.CHARGE, member.getEmail());

        return PointResponse.of(pointRepository.save(point));
    }

    public PointResponse findById(Long pointId, LoginUser loginUser) {
        Point point = pointRepository.findById(pointId).orElseThrow(() -> new NotFoundDataException(ErrorType.NOT_FOUND_DATA));
        Member member = memberService.findMemberByEmail(loginUser.getEmail());

        if (!point.isOwn(member)) {
            throw new NonMatchingMemberException(ErrorType.NON_MATCHING_MEMBER);
        }

        return PointResponse.of(point);
    }
}
