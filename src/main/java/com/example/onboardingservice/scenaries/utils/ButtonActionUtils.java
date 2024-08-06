package com.example.onboardingservice.scenaries.utils;

import com.example.onboardingservice.scenaries.model.impl.CallbackData;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@UtilityClass
public class ButtonActionUtils {

    private static final String SPLITERATOR = "_";

    public String generateButtonCallbackData(Long chatId, UUID actionId) {
        return "%d_%s".formatted(chatId, actionId);
    }

    public CallbackData parseButtonCallbackData(String data) {
        return Optional.ofNullable(data)
                .map(it -> it.split(SPLITERATOR))
                .filter(lengthBetween(2, 2))
                .map(ButtonActionUtils::mapToData)
                .orElseThrow(IllegalStateException::new);
    }

    private Predicate<String[]> lengthBetween(int left, int right) {
        return (array) -> array.length >= left && array.length <= right;
    }

    private CallbackData mapToData(String[] data) {
        return new CallbackData(Long.parseLong(data[0]), UUID.fromString(data[1]));
    }
}
