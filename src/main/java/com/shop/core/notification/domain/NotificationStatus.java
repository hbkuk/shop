package com.shop.core.notification.domain;

public enum NotificationStatus {
    READ,
    UNREAD,
    FAILED;

    public static boolean isReadable(NotificationStatus notificationStatus) {
        return notificationStatus != FAILED;
    }
}
