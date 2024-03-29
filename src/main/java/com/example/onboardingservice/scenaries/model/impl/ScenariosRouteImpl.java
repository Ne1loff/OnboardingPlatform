package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ScenariosRoute;
import com.example.onboardingservice.scenaries.actions.Action;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ScenariosRouteImpl implements ScenariosRoute {

    private UUID firstActionId;
    private UUID currentActionId;
    private Map<UUID, Action> actions;

    public static ScenariosRouteImplBuilder builder() {
        return new ScenariosRouteImplBuilder();
    }

    @Override
    public UUID getFirstActionId() {
        return firstActionId;
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

    public static class ScenariosRouteImplBuilder {

        private UUID firstActionId;
        private UUID currentActionId;
        private Map<UUID, Action> actions;

        public ScenariosRouteImplBuilder withActions(List<Action> actions) {
            this.actions = actions.stream().collect(Collectors.toMap(Action::getId, Function.identity()));
            return this;
        }

        public ScenariosRouteImplBuilder withFirstActionId(UUID actionId) {
            this.firstActionId = actionId;
            return this;
        }

        public ScenariosRouteImplBuilder withCurrentActionId(UUID actionId) {
            this.currentActionId = actionId;
            return this;
        }

        public ScenariosRoute build() {
            return new ScenariosRouteImpl(firstActionId, currentActionId, actions);
        }

    }
}
