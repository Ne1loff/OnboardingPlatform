package com.example.onboardingservice.scenaries.actions;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @Type(value = TextAction.class),
        @Type(value = ContactAction.class),
})
public sealed interface Action extends Serializable permits AbstractAction {

    UUID getId();

    String getName();

    Optional<UUID> getNextActionId();

    void onUpdate(final AbsSender sender, final Update update, final ActionContext context, final ScenariosMetadata metadata)
            throws TelegramApiException;
}
