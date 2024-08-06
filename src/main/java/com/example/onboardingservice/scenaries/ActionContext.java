package com.example.onboardingservice.scenaries;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionContext {
    private Long chatId;
    @Nullable
    private UUID actionId;
    @Nullable
    private Boolean startFromBegin;
    @Nullable
    private Boolean initNextScenarios;
    @Nullable
    private UUID nextScenarioRouteDefinitionId;
    private boolean includeTestScenarios;
    private Map<String, String> parameters;

    public ActionContext(Long chatId) {
        this.chatId = chatId;
        this.parameters = new HashMap<>();
    }

    public static ActionContext of(Long chatId) {
        return new ActionContext(chatId);
    }

    public String get(String key) {
        return parameters.get(key);
    }

    public void put(String key, Object value) {
        parameters.put(key, String.valueOf(value));
    }

    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    public ActionContext restore(ActionContext context) {
        if (context == null) {
            return this;
        }
        if (this.actionId == null) {
            this.actionId = context.actionId;
        }
        if (this.nextScenarioRouteDefinitionId == null) {
            this.nextScenarioRouteDefinitionId = context.nextScenarioRouteDefinitionId;
        }
        if (this.startFromBegin == null) {
            this.startFromBegin = context.startFromBegin;
        }
        if (this.initNextScenarios == null) {
            this.initNextScenarios = context.initNextScenarios;
        }
        this.includeTestScenarios = context.includeTestScenarios;

        context.getParameters().forEach((key, value) -> parameters.computeIfAbsent(key, (it) -> value));
        return this;
    }
}
