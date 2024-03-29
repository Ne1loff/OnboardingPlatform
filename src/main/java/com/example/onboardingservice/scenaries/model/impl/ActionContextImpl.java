package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ContextConstants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ActionContextImpl implements ActionContext {

    private final Long chatId;
    private final Map<String, Object> parameters = new HashMap<>();


    public static ActionContext of(Long chatId) {
        return new ActionContextImpl(chatId);
    }

    @Override
    public <T> T get(ContextConstants.ContextParameter<T> parameter) {
        return parameter.getParameterClass().cast(parameters.get(parameter.getKey()));
    }

    @Override
    public Object get(String key) {
        return parameters.get(key);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public <T> void put(ContextConstants.ContextParameter<T> parameter, T value) {
        parameters.put(parameter.getKey(), value);
    }

    @Override
    public void put(String key, Object value) {
        parameters.put(key, value);
    }

    @Override
    public boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    @Override
    public <T> boolean containsKey(ContextConstants.ContextParameter<T> parameter) {
        return parameters.containsKey(parameter.getKey());
    }

    @Override
    public ActionContext restore(ActionContext context) {
        context.getParameters().forEach((key, value) -> parameters.computeIfAbsent(key, (it) -> value));
        return this;
    }
}
