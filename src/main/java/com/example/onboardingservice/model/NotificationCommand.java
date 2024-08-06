package com.example.onboardingservice.model;

import com.example.onboardingservice.scenaries.model.enumeration.NotificationMode;

import java.time.Duration;

public record NotificationCommand(
        Long chatId,
        String text,
        Duration delay,
        NotificationMode mode
) {
    public static NotificationCommand of(Long chatId, String text, Duration delay, NotificationMode mode) {
        return new NotificationCommand(chatId, text, delay, mode);
    }
}
