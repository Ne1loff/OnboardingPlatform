package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.SendContactAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;

@Component
public class SendContactActionHandler implements ActionHandler<SendContactAction> {
    @Override
    public Class<SendContactAction> getHandledClass() {
        return SendContactAction.class;
    }

    @Override
    public Optional<UUID> process(SendContactAction action, AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        final Long chatId = context.getChatId();

        var message = SendContact.builder()
                .chatId(chatId)
                .phoneNumber(action.getPhoneNumber())
                .firstName(action.getFirstName())
                .lastName(action.getLastName())
                .build();
        sender.execute(message);

        return action.getNextActionId();
    }
}
