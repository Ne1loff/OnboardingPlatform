package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.ReadMessageAction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

@Component
public class ReadMessageActionHandler implements ActionHandler<ReadMessageAction> {

    private final Map<Long, Boolean> sendMessageStatus = new HashMap<>();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public Class<ReadMessageAction> getHandledClass() {
        return ReadMessageAction.class;
    }

    @Override
    public Optional<UUID> process(ReadMessageAction action, AbsSender sender, Update update, ActionContext context, ScenariosMetadata metadata) throws TelegramApiException {
        final Long chatId = context.getChatId();

        if (sendMessageStatus.computeIfAbsent(chatId, key -> false)) {
            var userMessage = update.getMessage().getText();

            if (action.getRegex() != null) {
                var regex = Pattern.compile(action.getRegex());
                var matcher = regex.matcher(userMessage);

                if (matcher.find()) {
                    var groupCount = matcher.groupCount();
                    userMessage = matcher.group(groupCount > 1 ? groupCount - 1 : groupCount);
                }
            }

            context.put(action.getProperty(), userMessage);

            sendMessageStatus.put(chatId, false);
        } else {
            sendMessageStatus.put(chatId, true);
        }

        return action.getNextActionId().filter(it -> !sendMessageStatus.getOrDefault(chatId, true));
    }

    private void sendTimeoutMessage(AbsSender sender, SendMessage sendMessage) {
        try {
            sender.execute(sendMessage);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
