package com.shop.core.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Type {
    NORMAL("NORMAL");

    private final String type;
}
