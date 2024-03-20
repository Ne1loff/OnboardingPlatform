package com.example.onboardingservice.scenaries.model;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.impl.RouteMatcher;
import com.example.onboardingservice.scenaries.model.impl.ScenariosRouteBlueprint;

import java.util.List;
import java.util.UUID;

public interface ScenariosRouteDescription {
    String getName();

    UUID getFirstActionId();

    List<RouteMatcher> getMatchers();

    ScenariosRouteBlueprint getRoute();

    ScenariosRoute build(ActionContext context);
}
