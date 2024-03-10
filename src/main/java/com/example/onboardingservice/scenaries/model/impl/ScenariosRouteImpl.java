package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.actions.Action;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ScenariosRouteImpl implements ScenariosRoute {

    private UUID currentActionId;
    private Map<UUID, Action> actions;

    public static ScenariosRoute build(Action[] actions) {
        var actionsMap = Arrays.stream(actions).collect(Collectors.toMap(Action::getId, Function.identity()));
        return new ScenariosRouteImpl(null, actionsMap);
    }

    @Override
    public boolean hasAction(UUID actionId) {
        return actions.containsKey(actionId);
    }

    @Override
    public Action next(UUID actionId) {
        var next = actions.get(actionId);

        if (next != null) {
            currentActionId = actionId;
        }

        return next;
    }

    @Override
    public Action current() {
        return actions.get(currentActionId);
    }
}
