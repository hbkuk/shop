package com.shop.core.notification.domain;

import com.shop.common.domain.base.BaseEntity;
import com.shop.common.exception.ErrorType;
import com.shop.core.member.domain.Member;
import com.shop.core.notification.exception.CannotNotificationReadException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @JoinColumn(name = "MEMBER_EMAIL")
    private String memberEmail;

    @JoinColumn(name = "ADMIN_EMAIL")
    private String adminEmail;

    public Notification(NotificationType notificationType, NotificationStatus notificationStatus, String memberEmail, String adminEmail) {
        this.notificationType = notificationType;
        this.notificationStatus = notificationStatus;
        this.memberEmail = memberEmail;
        this.adminEmail = adminEmail;
    }

    public void read() {
        if (!NotificationStatus.isReadable(notificationStatus)) {
            throw new CannotNotificationReadException(ErrorType.CANNOT_NOTIFICATION_READ);
        }

        this.notificationStatus = NotificationStatus.READ;
    }

    public boolean isOwn(Member member) {
        return memberEmail.equals(member.getEmail());
    }
}
