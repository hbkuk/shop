package com.shop.core.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    private List<String> memberEmails;

    private String adminEmail;

    private NotificationType notificationType;

    public static NotificationEvent of(List<String> memberEmails, String adminEmail, NotificationType notificationType) {
        return new NotificationEvent(memberEmails, adminEmail, notificationType);
    }
}
