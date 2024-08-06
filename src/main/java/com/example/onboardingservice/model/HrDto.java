package com.example.onboardingservice.model;

import org.jetbrains.annotations.Nullable;

public record HrDto(Long chatId, String username,
                    @Nullable String firstName,
                    @Nullable String lastName) {

    public static HrDto of(Long chatId, String username) {
        return of(chatId, username, null, null);
    }

    public static HrDto of(Long chatId, String username, String firstName, String lastName) {
        return new HrDto(chatId, username, firstName, lastName);
    }
}
