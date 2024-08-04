/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables.daos;


import com.example.onboardingservice.jooq.AbstractSpringDAOImpl;
import com.example.onboardingservice.jooq.tables.ScenarioNotification;
import com.example.onboardingservice.jooq.tables.records.ScenarioNotificationRecord;
import org.jooq.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
@Repository
public class ScenarioNotificationDao extends AbstractSpringDAOImpl<ScenarioNotificationRecord, com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification, UUID> {

    /**
     * Create a new ScenarioNotificationDao without any configuration
     */
    public ScenarioNotificationDao() {
        super(ScenarioNotification.SCENARIO_NOTIFICATION, com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification.class);
    }

    /**
     * Create a new ScenarioNotificationDao with an attached configuration
     */
    @Autowired
    public ScenarioNotificationDao(Configuration configuration) {
        super(ScenarioNotification.SCENARIO_NOTIFICATION, com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification.class, configuration);
    }

    @Override
    public UUID getId(com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchById(UUID... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification fetchOneById(UUID value) {
        return fetchOne(ScenarioNotification.SCENARIO_NOTIFICATION.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchOptionalById(UUID value) {
        return fetchOptional(ScenarioNotification.SCENARIO_NOTIFICATION.ID, value);
    }

    /**
     * Fetch records that have <code>chat_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfChatId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.CHAT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>chat_id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchByChatId(Long... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.CHAT_ID, values);
    }

    /**
     * Fetch records that have <code>message BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfMessage(String lowerInclusive, String upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.MESSAGE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>message IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchByMessage(String... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.MESSAGE, values);
    }

    /**
     * Fetch records that have <code>repeat BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfRepeat(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.REPEAT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>repeat IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchByRepeat(Boolean... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.REPEAT, values);
    }

    /**
     * Fetch records that have <code>delay BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfDelay(String lowerInclusive, String upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.DELAY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>delay IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchByDelay(String... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.DELAY, values);
    }

    /**
     * Fetch records that have <code>status BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfStatus(String lowerInclusive, String upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.STATUS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>status IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchByStatus(String... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.STATUS, values);
    }

    /**
     * Fetch records that have <code>next_execution BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchRangeOfNextExecution(OffsetDateTime lowerInclusive, OffsetDateTime upperInclusive) {
        return fetchRange(ScenarioNotification.SCENARIO_NOTIFICATION.NEXT_EXECUTION, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>next_execution IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification> fetchByNextExecution(OffsetDateTime... values) {
        return fetch(ScenarioNotification.SCENARIO_NOTIFICATION.NEXT_EXECUTION, values);
    }
}
