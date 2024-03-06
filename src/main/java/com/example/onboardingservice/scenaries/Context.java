package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.Scenario;
import com.example.onboardingservice.scenaries.actions.Action;

import java.io.Serializable;

public interface Context {

    Scenario getCurrentScenario();

    Action getCurrentAction();

}
