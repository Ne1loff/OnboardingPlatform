package com.example.onboardingservice.scenaries;

import com.example.onboardingservice.scenaries.ContextConstants.ContextParameter;

import java.util.Map;

public interface ActionContext {
    Long getChatId();

    <T> T get(ContextParameter<T> parameter);

    Object get(String key);

    Map<String, Object> getParameters();

    <T> void put(ContextParameter<T> parameter, T value);

    void put(String key, Object value);

    boolean containsKey(String key);

    <T> boolean containsKey(ContextParameter<T> parameter);

    ActionContext restore(ActionContext context);
}
