package com.example.onboardingservice.scenaries.utils;

import com.example.onboardingservice.scenaries.model.impl.CallbackData;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class ButtonActionUtils {

    private static final String SPLITERATOR = "_";
    private static final String INIT_PREFIX = "init";

    public String generateButtonCallbackData(Long chatId, String scenarioId, UUID actionId) {
        return "%d_%s_%s".formatted(chatId, scenarioId, actionId);
    }

    public String generateInitButtonCallbackData(Long chatId, String scenarioId, UUID actionId) {
        return "%s_%d_%s_%s".formatted(INIT_PREFIX, chatId, scenarioId, actionId);
    }

    public CallbackData parseButtonCallbackData(String data) {
        return Optional.ofNullable(data)
                .map(it -> it.split(SPLITERATOR))
                .filter(it -> it.length == 4)
                .map(it -> new CallbackData(Long.parseLong(it[1]), it[2], UUID.fromString(it[3]), INIT_PREFIX.equals(it[0])))
                .orElseThrow(IllegalStateException::new);
    }

}
