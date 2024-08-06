package com.example.onboardingservice.scenaries;

import java.util.UUID;

public interface ScenariosMetadata {

    UUID getScenarioId();

    String getScenarioName();

    UUID getRouteId();

    ScenariosRoute getRoute();

}
