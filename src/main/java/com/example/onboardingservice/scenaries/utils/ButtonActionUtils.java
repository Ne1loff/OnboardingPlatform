package com.example.onboardingservice.scenaries.utils;

import com.example.onboardingservice.scenaries.model.impl.CallbackData;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

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
                .filter(lengthBetween(3, 4))
                .map(ButtonActionUtils::mapToData)
                .orElseThrow(IllegalStateException::new);
    }

    private Predicate<String[]> lengthBetween(int left, int right) {
        return (array) -> array.length >= left && array.length <= right;
    }

    private CallbackData mapToData(String[] data) {
        if (data.length == 3) {
            return new CallbackData(Long.parseLong(data[0]), data[1], UUID.fromString(data[2]), false);
        } else {
            return new CallbackData(Long.parseLong(data[1]), data[2], UUID.fromString(data[3]), INIT_PREFIX.equals(data[0]));
        }
    }
}
