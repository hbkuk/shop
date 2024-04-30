package com.shop.core.notification.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.core.notification.domain.Notification;
import com.shop.core.notification.domain.NotificationStatus;
import com.shop.core.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    @JsonProperty("notification_type")
    private NotificationType type;

    @JsonProperty("notification_status")
    private NotificationStatus notificationStatus;

    public static NotificationResponse of(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getNotificationType(), notification.getNotificationStatus());
    }
}
