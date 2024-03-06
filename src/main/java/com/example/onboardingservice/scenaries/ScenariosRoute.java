package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.actions.Action;

import java.util.UUID;

public interface ScenariosRoute {

    boolean hasAction(UUID actionId);

    Action next(UUID actionId);

    Action current();

}
