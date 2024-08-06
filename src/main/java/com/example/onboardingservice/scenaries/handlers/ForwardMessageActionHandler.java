package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.ForwardMessageAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.UUID;

@Component
public class ForwardMessageActionHandler implements ActionHandler<ForwardMessageAction> {
    @Override
    public Class<ForwardMessageAction> getHandledClass() {
        return ForwardMessageAction.class;
    }

    @Override
    public Optional<UUID> process(ForwardMessageAction action,
                                  AbsSender sender,
                                  Update update,
                                  ActionContext context,
                                  ScenariosMetadata metadata) throws TelegramApiException {
        final Long chatId = context.getChatId();

        var message = ForwardMessage.builder()
                .chatId(action.getForwardChatId())
                .fromChatId(chatId)
                .messageId(update.getMessage().getMessageId())
                .build();
        sender.execute(message);

        return action.getNextActionId();
    }
}
