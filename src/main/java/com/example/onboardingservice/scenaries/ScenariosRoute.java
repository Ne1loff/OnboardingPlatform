package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.actions.Action;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(as = ScenariosRouteImpl.class)
public interface ScenariosRoute {

    UUID getFirstActionId();

    boolean hasAction(UUID actionId);

    Action next(UUID actionId);

    Action current();

}
