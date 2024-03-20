package com.example.onboardingservice.scenaries.model.impl;

import java.util.UUID;

public record CallbackData(Long chatId, String scenariosName, UUID actionId, boolean isInitScenarios) {}
