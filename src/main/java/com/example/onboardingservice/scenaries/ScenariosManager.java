package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;

public interface ScenariosManager {

    void createScenarios(ScenariosRouteDescription description);

    void deleteScenarios(String scenariosName);
}
