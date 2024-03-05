package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.actions.Action;

public interface Scenario {

    String getId();

    String getEvent();

    Action getCurrentAction();

    Action findNextAction();

}
