package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ScenariosMetadataImpl implements ScenariosMetadata {

    @Getter
    private final UUID scenarioId;
    @Getter
    private final ScenariosRoute route;

    @Override
    public String getScenarioName() {
        return null;
    }
}
