package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ScenariosMetadataImpl implements ScenariosMetadata {
    private final UUID scenarioId;
    private final String scenarioName;
    private final UUID routeId;
    private final ScenariosRoute route;
}
