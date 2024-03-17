package com.example.onboardingservice.scenaries.utils;

import com.example.onboardingservice.scenaries.model.impl.CallbackData;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class ButtonActionUtils {

    private  static final String SPLITERATOR = "_";

    public String generateButtonCallbackData(Long chatId, String scenarioId, UUID actionId) {
        return "%d_%s_%s".formatted(chatId, scenarioId, actionId);
    }

    public CallbackData parseButtonCallbackData(String data) {
        return Optional.ofNullable(data)
                .map(it -> it.split(SPLITERATOR))
                .filter(it -> it.length == 3)
                .map(it -> new CallbackData(Long.parseLong(it[0]), it[1], UUID.fromString(it[2])))
                .orElseThrow(IllegalStateException::new);
    }

}
