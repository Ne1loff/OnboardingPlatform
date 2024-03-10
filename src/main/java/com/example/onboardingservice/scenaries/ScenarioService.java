package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.actions.Action;

public interface ScenarioService {

    ActionContext buildContext(Long chatId);

    ScenariosMetadata initializeScenarios(ActionContext context);

    Action buildCurrentAction(ScenariosMetadata metadata, ActionContext context);

    ScenariosMetadata findActiveScenariosMetadata(Long chatId);
}
