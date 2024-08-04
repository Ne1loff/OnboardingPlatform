package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class ScenariosRouteDescriptionImpl implements ScenariosRouteDescription {

    private UUID id;
    private String name;
    private UUID firstActionId;
    private RouteMatcher matcher;
    private ScenariosStatus status;
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

        if (status == ScenariosStatus.TEST && !Boolean.parseBoolean(context.get(ContextConstants.INCLUDE_TEST_SCENARIOS))) {
            return null;
        }

        var type = matcher.getType();
        if (!context.containsKey(type.name())) {
            return null;
        }

        if (!context.get(type.name()).equals(matcher.getValue())) {
            return null;
        }

        return ScenariosRouteImpl.builder()
                .withFirstActionId(firstActionId)
                .withActions(route.getActions())
                .withCurrentActionId(firstActionId)
                .build();
    }
}
