package com.shop.common.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Event {

    private static ApplicationEventPublisher applicationEventPublisher;

    static void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        Event.applicationEventPublisher = applicationEventPublisher;
    }

    public static void publish(final Object event) {
        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(event);
        }
    }
}
