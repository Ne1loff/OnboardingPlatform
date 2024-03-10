package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.RouteMatcher;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import lombok.Data;

@Data
public class ScenariosRouteDescriptionImpl implements ScenariosRouteDescription {

    private String name;
    private RouteMatcher[] matchers;
    private ScenariosRouteBlueprint route;

    @Override
    public ScenariosRoute build(ActionContext context) {
        var parameters = context.getParameters();

        for (RouteMatcher matcher : matchers) {
            var type = matcher.getType();

            if (!parameters.containsKey(type.name())) {
                continue;
            }

            if (!parameters.get(type.name()).equals(matcher.getValue())) {
                continue;
            }

            return ScenariosRouteImpl.build(route.getActions());
        }
        return null;
    }
}
