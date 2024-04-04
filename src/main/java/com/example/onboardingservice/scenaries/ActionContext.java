package com.example.onboardingservice.scenaries;

import java.util.Map;

public interface ActionContext {
    Long getChatId();

    String get(String key);

    Map<String, String> getParameters();

    void put(String key, Object value);

    boolean containsKey(String key);

    ActionContext restore(ActionContext context);
}
