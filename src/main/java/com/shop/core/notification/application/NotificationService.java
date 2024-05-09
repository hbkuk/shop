package com.shop.core.notification.application;

import com.shop.common.exception.ErrorType;
import com.shop.core.admin.auth.application.AdminAuthService;
import com.shop.core.auth.domain.LoginUser;
import com.shop.core.member.application.MemberService;
import com.shop.core.member.domain.Member;
import com.shop.core.member.exception.NonMatchingMemberException;
import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationRepository;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import com.shop.core.notification.exception.NotFoundNotificationException;
import com.shop.core.notification.presentation.dto.NotificationRequest;
import com.shop.core.notification.presentation.dto.NotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final AdminAuthService adminAuthService;

    private final MemberService memberService;

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationResponse send(NotificationRequest request, LoginUser loginAdmin) {
        verifyAdmin(loginAdmin);
        verifyMemberByEmail(request.getMemberEmail());

        Notification notification = new Notification(request.getNotificationType(), NotificationStatus.UNREAD, request.getMemberEmail(), loginAdmin.getEmail());
        return NotificationResponse.of(notificationRepository.save(notification));
    }

    @Transactional
    public void send(List<String> memberEmails, String adminEmail, NotificationType notificationType) {
        memberEmails.forEach(memberEmail -> send(NotificationRequest.of(notificationType, memberEmail), LoginUser.of(adminEmail)));
    }

    @Transactional
    public void read(Long notificationId, LoginUser loginMember) {
        Member member = verifyMemberByEmail(loginMember.getEmail());

        Notification notification = findNotificationById(notificationId);
        if (!notification.isOwn(member)) {
            throw new NonMatchingMemberException(ErrorType.NON_MATCHING_MEMBER);
        }
        notification.read();
    }

    public NotificationResponse findById(Long notificationId, LoginUser loginMember) {
        Member member = verifyMemberByEmail(loginMember.getEmail());

        Notification notification = findNotificationById(notificationId);
        if (!notification.isOwn(member)) {
            throw new NonMatchingMemberException(ErrorType.NON_MATCHING_MEMBER);
        }

        return NotificationResponse.of(notification);
    }

    public List<NotificationResponse> findAll(LoginUser loginAdmin) {
        verifyAdmin(loginAdmin);

        List<Notification> notifications = notificationRepository.findAll();
        return NotificationResponse.of(notifications);
    }

    private void verifyAdmin(LoginUser loginUser) {
        adminAuthService.findAdminByEmail(loginUser.getEmail());
    }

    private Member verifyMemberByEmail(String email) {
        return memberService.findMemberByEmail(email);
    }

    private Notification findNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundNotificationException(ErrorType.NOT_FOUND_NOTIFICATION));
    }
}
