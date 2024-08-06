package com.example.onboardingservice.model.documents;

import java.util.UUID;

public record DocumentDto(UUID id, String filename) {
    public static DocumentDto of(UUID id, String filename) {
        return new DocumentDto(id, filename);
    }
}
