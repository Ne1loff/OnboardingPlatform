package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.Scenario;
import com.example.onboardingservice.scenaries.actions.Action;

import java.io.Serializable;

public interface Context extends Serializable {

    Long getChatId();

    Scenario getCurrentScenario();

    Action getCurrentAction();

}
