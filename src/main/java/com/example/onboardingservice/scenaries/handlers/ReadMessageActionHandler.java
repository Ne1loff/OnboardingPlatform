package com.example.onboardingservice.scenaries.handlers;

import com.example.onboardingservice.model.NotificationCommand;
import com.example.onboardingservice.model.NotificationStatus;
import com.example.onboardingservice.scenaries.ActionContext;
import com.example.onboardingservice.scenaries.ScenariosMetadata;
import com.example.onboardingservice.scenaries.actions.impl.ReadMessageAction;
import com.example.onboardingservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ReadMessageActionHandler implements ActionHandler<ReadMessageAction> {

    private final NotificationsService notificationsService;
    private final Map<Long, Boolean> sendMessageStatus = new HashMap<>();

    @Override
    public Class<ReadMessageAction> getHandledClass() {
        return ReadMessageAction.class;
    }

    @Override
    public Optional<UUID> process(ReadMessageAction action,
                                  AbsSender sender,
                                  Update update,
                                  ActionContext context,
                                  ScenariosMetadata metadata) throws TelegramApiException {
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

            if (context.containsKey("NOTIFICATION_ID")) {
                notificationsService.updateNotificationStatus(UUID.fromString(context.get("NOTIFICATION_ID")), NotificationStatus.DISABLE);
            }

            sendMessageStatus.put(chatId, false);
        } else {
            sendMessageStatus.put(chatId, true);
            if (!action.getNotificationMode().isDisabled()) {
                var command = NotificationCommand.of(
                        context.getChatId(),
                        action.getTimeoutMessage(),
                        action.getWaitingTime(),
                        action.getNotificationMode());
                var notificationId = notificationsService.registerNotification(command);
                context.put("NOTIFICATION_ID", notificationId);
            }
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
