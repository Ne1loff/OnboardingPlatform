package com.example.onboardingservice.scenaries;

import java.util.Map;

public interface ActionContext {
    Long getChatId();

    Map<String, Object> getParameters();

    void putParameter(String key, Object value);
}
