package com.example.onboardingservice.scenaries;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ScenarioService {

    ActionContext buildContext(Long chatId);

    @NotNull
    ScenariosMetadata initializeScenarios(ActionContext context);

    @Nullable
    ScenariosMetadata findActiveScenariosMetadata(ActionContext context);

    @Nullable
    ActionContext findActiveContext(Long chatId);

    void saveScenariosMetadata(ActionContext context, ScenariosMetadata metadata);

    ScenariosMetadata changeScenarios(ActionContext context, ScenariosMetadata metadata);
}
