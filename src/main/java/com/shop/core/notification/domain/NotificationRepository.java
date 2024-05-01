package com.shop.core.notification.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.notificationType= :notificationType")
    List<Notification> findByNotificationType(@Param("notificationType") NotificationType notificationType);
}
