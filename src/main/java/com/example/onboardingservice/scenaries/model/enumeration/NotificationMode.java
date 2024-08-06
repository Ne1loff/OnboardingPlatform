package com.example.onboardingservice.scenaries.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMode {
    ONCE(false),
    EVERY(false),
    DISABLED(true);

    private final boolean disabled;
}
