package com.shop.core.notification.presentation;

import com.shop.core.memberAuth.domain.LoginUser;
import com.shop.core.memberAuth.presentation.AuthenticationPrincipal;
import com.shop.core.notification.application.NotificationService;
import com.shop.core.notification.presentation.dto.NotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/{notificationId}/receive")
    public ResponseEntity<Void> read(@PathVariable Long notificationId,
                                     @AuthenticationPrincipal LoginUser loginMember) {
        notificationService.read(notificationId, loginMember);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable Long notificationId,
                                                         @AuthenticationPrincipal LoginUser loginMember) {
        NotificationResponse response = notificationService.findById(notificationId, loginMember);
        return ResponseEntity.ok(response);
    }

    // TODO: admin controller 이동
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> findAll(@AuthenticationPrincipal LoginUser loginAdmin) {
        List<NotificationResponse> response = notificationService.findAll(loginAdmin);
        return ResponseEntity.ok(response);
    }
}
