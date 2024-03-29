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

        if (context.containsKey(ContextConstants.SCENARIOS_NAME)) {
            if (context.get(ContextConstants.SCENARIOS_NAME).equals(name)) {
                return ScenariosRouteImpl.builder()
                        .withFirstActionId(firstActionId)
                        .withActions(route.getActions())
                        .withCurrentActionId(firstActionId)
                        .build();
            } else {
                return null;
            }
        }

        for (RouteMatcher matcher : matchers) {
            var type = matcher.getType();

            if (!context.containsKey(type.name())) {
                continue;
            }

            if (!context.get(type.name()).equals(matcher.getValue())) {
                continue;
            }

            return ScenariosRouteImpl.builder()
                    .withFirstActionId(firstActionId)
                    .withActions(route.getActions())
                    .withCurrentActionId(firstActionId)
                    .build();
        }
        return null;
    }
}
