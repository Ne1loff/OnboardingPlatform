package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ScenariosRouteDescriptionImpl implements ScenariosRouteDescription {

    private String name;
    private UUID firstActionId;
    private List<RouteMatcher> matchers;
    private ScenariosRouteBlueprint route;

    @Override
    public ScenariosRoute build(ActionContext context) {
        var parameters = context.getParameters();

        if (parameters.containsKey(ContextConstants.SCENARIOS_NAME)) {
            if (parameters.get(ContextConstants.SCENARIOS_NAME).equals(name)) {
                return ScenariosRouteImpl.builder()
                        .withActions(route.getActions())
                        .withCurrentActionId(firstActionId)
                        .build();
            } else {
                return null;
            }
        }

        for (RouteMatcher matcher : matchers) {
            var type = matcher.getType();

            if (!parameters.containsKey(type.name())) {
                continue;
            }

            if (!parameters.get(type.name()).equals(matcher.getValue())) {
                continue;
            }

            return ScenariosRouteImpl.builder()
                    .withActions(route.getActions())
                    .withCurrentActionId(firstActionId)
                    .build();
        }
        return null;
    }
}
