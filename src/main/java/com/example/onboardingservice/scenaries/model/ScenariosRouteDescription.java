package com.example.onboardingservice.scenaries.model;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStatus;
import com.example.onboardingservice.scenaries.model.impl.RouteMatcher;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteBlueprint;

import java.util.UUID;

public interface ScenariosRouteDescription {

    UUID getId();

    String getName();

    UUID getFirstActionId();

    RouteMatcher getMatcher();

    ScenariosRouteBlueprint getRoute();

    ScenariosStatus getStatus();

    ScenariosRoute build(ActionContext context);
}
