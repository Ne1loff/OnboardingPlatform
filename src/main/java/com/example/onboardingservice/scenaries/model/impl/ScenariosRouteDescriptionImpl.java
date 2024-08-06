package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.model.ScenariosRouteDescription;
import com.example.onboardingservice.scenaries.model.enumeration.ScenariosStatus;
import lombok.Data;

import java.util.Optional;
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

        var nextScenariosIdOptional = Optional.ofNullable(context.getNextScenarioRouteDefinitionId());
        if (nextScenariosIdOptional.isPresent()) {
            if (id.equals(nextScenariosIdOptional.get())) {
                return ScenariosRouteImpl.builder()
                        .withFirstActionId(firstActionId)
                        .withActions(route.getActions())
                        .withCurrentActionId(firstActionId)
                        .build();
            } else {
                return null;
            }
        }

        if (status == ScenariosStatus.TEST && !context.isIncludeTestScenarios()) {
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
