package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.scenaries.Context;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.UUID;

@JsonSubTypes({
        @Type(value = TextAction.class)
})
public sealed interface Action extends Serializable permits TextAction {

    UUID getId();

    String getName();

    void onUpdate(final Update update, final AbsSender sender, final Context context) throws TelegramApiException;
}
