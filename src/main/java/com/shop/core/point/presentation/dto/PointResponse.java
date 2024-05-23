package com.shop.core.point.presentation.dto;

import com.shop.core.point.domain.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointResponse {

    private Long id;

    private int amount;

    public static PointResponse of(Point point) {
        return new PointResponse(point.getId(), point.getAmount());
    }
}
