package com.shop.core.point.presentation;

import com.shop.common.auth.AuthenticationPrincipal;
import com.shop.common.domain.auth.LoginUser;
import com.shop.core.point.application.PointService;
import com.shop.core.point.presentation.dto.PaymentWebhookRequest;
import com.shop.core.point.presentation.dto.PointResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/payment-webhook/points")
    public ResponseEntity<PointResponse> receivePointWebhook(@RequestBody PaymentWebhookRequest request) {
        PointResponse pointResponse = pointService.receivePointWebhook(request);
        return ResponseEntity.created(URI.create("/points/" + pointResponse.getId())).build();
    }

    @GetMapping("/points/{pointId}")
    public ResponseEntity<PointResponse> findById(@PathVariable Long pointId,
                                                  @AuthenticationPrincipal LoginUser loginUser) {
        PointResponse response = pointService.findById(pointId, loginUser);
        return ResponseEntity.ok(response);
    }
}
