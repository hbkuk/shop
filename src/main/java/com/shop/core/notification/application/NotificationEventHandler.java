package com.shop.core.notification.application;

import com.shop.core.notification.domain.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @EventListener
    public void onNotificationEvent(NotificationEvent event) {
        notificationService.send(event.getMemberEmails(), event.getAdminEmail(), event.getNotificationType());
    }
}
