package com.shop.core.notification.presentation.dto;

import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    private NotificationType notificationType;

    private String memberEmail;

    public static NotificationRequest of(NotificationType type, String memberEmail) {
        return new NotificationRequest(type, memberEmail);
    }

    public Notification toEntity(String adminEmail) {
        return new Notification(notificationType, NotificationStatus.UNREAD, memberEmail, adminEmail);
    }
}
