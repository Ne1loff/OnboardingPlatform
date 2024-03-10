package com.example.onboardingservice.scenaries.model.impl;

import com.example.onboardingservice.scenaries.ActionContext;
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
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void putParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
