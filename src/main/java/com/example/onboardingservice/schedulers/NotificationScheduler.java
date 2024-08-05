package com.example.onboardingservice.schedulers;

import com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification;
import com.example.onboardingservice.model.NotificationStatus;
import com.example.onboardingservice.service.NotificationNotifier;
import com.example.onboardingservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private static final long FIVE_SECONDS_DELAY = 5000L;
    private static final long FIVE_MIN_DELAY = 300_000L;

    private final NotificationsService notificationsService;
    private final NotificationNotifier notificationNotifier;

    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);

    @Scheduled(fixedDelay = NotificationScheduler.FIVE_SECONDS_DELAY)
    public void findNotifications() {
        log.info("Ищем доступные уведомления...");
        var nextExecutionTime = LocalDateTime.now().plus(FIVE_SECONDS_DELAY, ChronoUnit.MILLIS);
        var notifications = notificationsService.findNotificationsForNext(nextExecutionTime);

        notifications.forEach(this::prepareExecution);
    }

    @Scheduled(fixedDelay = NotificationScheduler.FIVE_MIN_DELAY)
    public void deleteNonActiveNotifications() {
        log.info("Удаляем неактуальные уведомления...");
        var deletableStatuses = List.of(NotificationStatus.DONE, NotificationStatus.DISABLE);
        var notificationsForDelete = notificationsService.findNotificationsByStatuses(deletableStatuses);

        notificationsService.deleteNotifications(notificationsForDelete.stream().map(ScenarioNotification::getId).toList());
    }

    private void prepareExecution(ScenarioNotification notification) {
        var delay = Math.max(OffsetDateTime.now().until(notification.getNextExecution(), ChronoUnit.MILLIS), 0);
        scheduledExecutor.schedule(() -> notificationNotifier.notify(notification), delay, TimeUnit.MILLISECONDS);
    }
}
