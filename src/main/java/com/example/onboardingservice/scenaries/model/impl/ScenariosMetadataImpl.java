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
    private final String scenarioName;
    @Getter
    private final ScenariosRoute route;
}
