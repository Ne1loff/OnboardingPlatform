package com.example.onboardingservice.scenaries.impl;

import com.example.onboardingservice.scenaries.Scenario;
import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.Context;
import lombok.Data;

@Data
public class ContextImpl implements Context {

    private Long chatId;
    private Scenario currentScenario;

    public Action getCurrentAction() {
        return currentScenario.getCurrentAction();
    }
}
