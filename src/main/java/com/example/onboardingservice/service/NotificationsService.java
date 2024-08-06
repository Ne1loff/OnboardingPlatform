package com.example.onboardingservice.service;

import com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification;
import com.example.onboardingservice.model.NotificationCommand;
import com.example.onboardingservice.model.NotificationStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface NotificationsService {

    UUID registerNotification(NotificationCommand command);

    List<ScenarioNotification> findNotificationsForNext(LocalDateTime time);

    List<ScenarioNotification> findNotificationsByStatuses(Collection<NotificationStatus> statuses);

    void updateNotificationNextExecution(ScenarioNotification notification);

    void updateNotificationStatus(UUID notificationId, NotificationStatus status);

    boolean notificationExists(UUID notificationId);

    void deleteNotifications(List<UUID> notificationIds);
}
