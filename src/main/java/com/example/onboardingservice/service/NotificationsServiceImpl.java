package com.example.onboardingservice.service;

import com.example.onboardingservice.jooq.Tables;
import com.example.onboardingservice.jooq.tables.daos.ScenarioNotificationDao;
import com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification;
import com.example.onboardingservice.model.NotificationCommand;
import com.example.onboardingservice.model.NotificationStatus;
import com.example.onboardingservice.scenaries.model.enumeration.NotificationMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NotificationsServiceImpl implements NotificationsService {
    private static final ZoneOffset SYSTEM_OFFSET = OffsetDateTime.now().getOffset();
    private final ScenarioNotificationDao notificationDao;

    @Override
    @Transactional
    public UUID registerNotification(NotificationCommand command) {
        var notificationId = UUID.randomUUID();

        var notification = new ScenarioNotification();
        notification.setId(notificationId);
        notification.setChatId(command.chatId());
        notification.setRepeat(command.mode() == NotificationMode.EVERY);
        notification.setDelay(command.delay().toString());
        notification.setMessage(command.text());
        notification.setStatus(NotificationStatus.WAITING.toString());
        notification.setNextExecution(calculateNextExecution(OffsetDateTime.now(), command.delay()));

        notificationDao.insert(notification);

        return notificationId;
    }

    @Override
    @Transactional
    public void updateNotificationStatus(UUID notificationId, NotificationStatus status) {
        var notification = notificationDao.findOptionalById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus(status.toString());
        notificationDao.update(notification);
    }

    @Override
    public boolean notificationExists(UUID notificationId) {
        return notificationDao.existsById(notificationId);
    }

    @Override
    @Transactional
    public void deleteNotifications(List<UUID> notificationIds) {
        notificationDao.deleteById(notificationIds);
    }

    @Override
    public List<ScenarioNotification> findNotificationsForNext(LocalDateTime time) {
        return notificationDao.ctx().selectFrom(Tables.SCENARIO_NOTIFICATION)
                .where(Tables.SCENARIO_NOTIFICATION.STATUS.eq(NotificationStatus.WAITING.toString())
                        .and(Tables.SCENARIO_NOTIFICATION.NEXT_EXECUTION.between(OffsetDateTime.now().minusHours(3), time.atOffset(SYSTEM_OFFSET))))
                .orderBy(Tables.SCENARIO_NOTIFICATION.NEXT_EXECUTION.asc())
                .fetchStream()
                .map(it -> it.into(ScenarioNotification.class))
                .toList();
    }

    @Override
    public List<ScenarioNotification> findNotificationsByStatuses(Collection<NotificationStatus> statuses) {
        var matchingStatuses = statuses.stream().map(Enum::toString).toArray(String[]::new);
        return notificationDao.fetchByStatus(matchingStatuses);
    }

    @Override
    @Transactional
    public void updateNotificationNextExecution(ScenarioNotification notification) {
        notification.setNextExecution(calculateNextExecution(notification.getNextExecution(), Duration.parse(notification.getDelay())));
        notificationDao.update(notification);
    }

    private OffsetDateTime calculateNextExecution(LocalDateTime current, Duration delay) {
        return calculateNextExecution(current.atOffset(SYSTEM_OFFSET), delay);
    }

    private OffsetDateTime calculateNextExecution(OffsetDateTime current, Duration delay) {
        return current.plus(delay);
    }
}
