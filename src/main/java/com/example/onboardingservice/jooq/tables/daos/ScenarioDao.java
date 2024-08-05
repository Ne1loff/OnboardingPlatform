/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables.daos;


import com.example.onboardingservice.jooq.AbstractSpringDAOImpl;
import com.example.onboardingservice.jooq.tables.Scenario;
import com.example.onboardingservice.jooq.tables.records.ScenarioRecord;
import org.jooq.Configuration;
import org.jooq.JSONB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
@Repository
public class ScenarioDao extends AbstractSpringDAOImpl<ScenarioRecord, com.example.onboardingservice.jooq.tables.pojos.Scenario, UUID> {

    /**
     * Create a new ScenarioDao without any configuration
     */
    public ScenarioDao() {
        super(Scenario.SCENARIO, com.example.onboardingservice.jooq.tables.pojos.Scenario.class);
    }

    /**
     * Create a new ScenarioDao with an attached configuration
     */
    @Autowired
    public ScenarioDao(Configuration configuration) {
        super(Scenario.SCENARIO, com.example.onboardingservice.jooq.tables.pojos.Scenario.class, configuration);
    }

    @Override
    public UUID getId(com.example.onboardingservice.jooq.tables.pojos.Scenario object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(Scenario.SCENARIO.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchById(UUID... values) {
        return fetch(Scenario.SCENARIO.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.example.onboardingservice.jooq.tables.pojos.Scenario fetchOneById(UUID value) {
        return fetchOne(Scenario.SCENARIO.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchOptionalById(UUID value) {
        return fetchOptional(Scenario.SCENARIO.ID, value);
    }

    /**
     * Fetch records that have <code>scenario_name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfScenarioName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Scenario.SCENARIO.SCENARIO_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>scenario_name IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByScenarioName(String... values) {
        return fetch(Scenario.SCENARIO.SCENARIO_NAME, values);
    }

    /**
     * Fetch records that have <code>chat_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfChatId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Scenario.SCENARIO.CHAT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>chat_id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByChatId(Long... values) {
        return fetch(Scenario.SCENARIO.CHAT_ID, values);
    }

    /**
     * Fetch records that have <code>current_action_id BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfCurrentActionId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(Scenario.SCENARIO.CURRENT_ACTION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>current_action_id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByCurrentActionId(UUID... values) {
        return fetch(Scenario.SCENARIO.CURRENT_ACTION_ID, values);
    }

    /**
     * Fetch records that have <code>is_active BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfIsActive(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Scenario.SCENARIO.IS_ACTIVE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>is_active IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByIsActive(Boolean... values) {
        return fetch(Scenario.SCENARIO.IS_ACTIVE, values);
    }

    /**
     * Fetch records that have <code>first_action_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfFirstActionId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(Scenario.SCENARIO.FIRST_ACTION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>first_action_id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByFirstActionId(UUID... values) {
        return fetch(Scenario.SCENARIO.FIRST_ACTION_ID, values);
    }

    /**
     * Fetch records that have <code>context BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfContext(JSONB lowerInclusive, JSONB upperInclusive) {
        return fetchRange(Scenario.SCENARIO.CONTEXT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>context IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByContext(JSONB... values) {
        return fetch(Scenario.SCENARIO.CONTEXT, values);
    }

    /**
     * Fetch records that have <code>route_definition_id BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchRangeOfRouteDefinitionId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(Scenario.SCENARIO.ROUTE_DEFINITION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>route_definition_id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.Scenario> fetchByRouteDefinitionId(UUID... values) {
        return fetch(Scenario.SCENARIO.ROUTE_DEFINITION_ID, values);
    }
}
