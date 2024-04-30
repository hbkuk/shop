package com.shop.core.notification.presentation.dto;

import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private NotificationType type;

    private String message;

    private String memberEmail;

    public static NotificationRequest of(NotificationType type, String message, String memberEmail) {
        return new NotificationRequest(type, message, memberEmail);
    }

    public Notification toEntity(LocalDateTime notificationAt, String adminEmail) {
        return new Notification(type, message, notificationAt, NotificationStatus.UNREAD, memberEmail, adminEmail);
    }
}
