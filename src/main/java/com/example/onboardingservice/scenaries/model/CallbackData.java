package com.example.onboardingservice.scenaries.model;

import java.util.UUID;

public record CallbackData(Long chatId, String scenarioId, UUID actionId) {}
