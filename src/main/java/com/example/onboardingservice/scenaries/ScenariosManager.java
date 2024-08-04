package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.model.ScenariosCreateResponse;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteDescriptionImpl;

import java.util.List;
import java.util.UUID;

public interface ScenariosManager {

    UUID createScenarios(ScenariosRouteDescription description);

    void deleteScenarios(String scenariosName);

    List<ScenariosRouteDescription> getAllScenarios();

    ScenariosRouteDescription getScenarioById(UUID scenarioId);

    ScenariosCreateResponse updateScenarios(UUID scenarioId, ScenariosRouteDescriptionImpl scenarios);
}
