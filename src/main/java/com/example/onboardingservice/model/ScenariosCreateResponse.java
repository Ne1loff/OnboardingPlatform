package com.example.onboardingservice.model;

import java.util.UUID;

public record ScenariosCreateResponse(UUID scenariosId) {
    public static ScenariosCreateResponse of(UUID scenariosId) {
        return new ScenariosCreateResponse(scenariosId);
    }
}
