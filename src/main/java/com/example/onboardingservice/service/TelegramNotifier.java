package com.example.onboardingservice.service;

import com.example.onboardingservice.bot.messages.MessageFactory;
import com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification;
import com.example.onboardingservice.model.NotificationStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class TelegramNotifier implements NotificationNotifier{

    private final AbsSender sender;
    private final NotificationsService notificationsService;

    @Override
    @SneakyThrows
    @Transactional
    public void notify(ScenarioNotification notification) {
        if (!notificationsService.notificationExists(notification.getId())) {
            return;
        }

        var message = MessageFactory.createSendMessage(notification.getChatId(), notification.getMessage());
        sender.execute(message);

        if (notification.getRepeat()) {
            notificationsService.updateNotificationNextExecution(notification);
        } else {
            notificationsService.updateNotificationStatus(notification.getId(), NotificationStatus.DONE);
        }
    }

}
