package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ActionContextImpl implements ActionContext {

    private Long chatId;
    private Map<String, String> parameters;

    public ActionContextImpl(Long chatId) {
        this.chatId = chatId;
        this.parameters = new HashMap<>();
    }

    public static ActionContext of(Long chatId) {
        return new ActionContextImpl(chatId);
    }

    @Override
    public String get(String key) {
        return parameters.get(key);
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }


    @Override
    public void put(String key, Object value) {
        parameters.put(key, String.valueOf(value));
    }

    @Override
    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    @Override
    public ActionContext restore(ActionContext context) {
        context.getParameters().forEach((key, value) -> parameters.computeIfAbsent(key, (it) -> value));
        return this;
    }
}
