package com.example.onboardingservice.scenaries.model;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteBlueprint;

import java.util.UUID;

public interface ScenariosRouteDescription {
    String getName();

    UUID getFirstActionId();

    RouteMatcher[] getMatchers();

    ScenariosRouteBlueprint getRoute();

    ScenariosRoute build(ActionContext context);
}
