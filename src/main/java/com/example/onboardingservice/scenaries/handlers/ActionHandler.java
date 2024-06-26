package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.Action;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;

public interface ActionHandler<T extends Action> {

    Class<T> getHandledClass();

    Optional<UUID> process(T action,
                           AbsSender sender,
                           Update update,
                           ActionContext context,
                           ScenariosMetadata metadata) throws TelegramApiException;

}
