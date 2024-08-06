/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables.daos;


import com.example.onboardingservice.jooq.AbstractSpringDAOImpl;
import com.example.onboardingservice.jooq.tables.ScenarioRouteDefinition;
import com.example.onboardingservice.jooq.tables.records.ScenarioRouteDefinitionRecord;
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
public class ScenarioRouteDefinitionDao extends AbstractSpringDAOImpl<ScenarioRouteDefinitionRecord, com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition, UUID> {

    /**
     * Create a new ScenarioRouteDefinitionDao without any configuration
     */
    public ScenarioRouteDefinitionDao() {
        super(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION, com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition.class);
    }

    /**
     * Create a new ScenarioRouteDefinitionDao with an attached configuration
     */
    @Autowired
    public ScenarioRouteDefinitionDao(Configuration configuration) {
        super(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION, com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition.class, configuration);
    }

    @Override
    public UUID getId(com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>scenario_name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchRangeOfScenarioName(String lowerInclusive, String upperInclusive) {
        return fetchRange(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>scenario_name IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchByScenarioName(String... values) {
        return fetch(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.SCENARIO_NAME, values);
    }

    /**
     * Fetch records that have <code>first_action_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchRangeOfFirstActionId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.FIRST_ACTION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>first_action_id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchByFirstActionId(UUID... values) {
        return fetch(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.FIRST_ACTION_ID, values);
    }

    /**
     * Fetch records that have <code>route_source BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchRangeOfRouteSource(JSONB lowerInclusive, JSONB upperInclusive) {
        return fetchRange(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>route_source IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchByRouteSource(JSONB... values) {
        return fetch(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.ROUTE_SOURCE, values);
    }

    /**
     * Fetch records that have <code>matcher BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchRangeOfMatcher(JSONB lowerInclusive, JSONB upperInclusive) {
        return fetchRange(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.MATCHER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>matcher IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchByMatcher(JSONB... values) {
        return fetch(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.MATCHER, values);
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchRangeOfId(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchById(UUID... values) {
        return fetch(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition fetchOneById(UUID value) {
        return fetchOne(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchOptionalById(UUID value) {
        return fetchOptional(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.ID, value);
    }

    /**
     * Fetch records that have <code>status BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchRangeOfStatus(String lowerInclusive, String upperInclusive) {
        return fetchRange(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.STATUS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>status IN (values)</code>
     */
    public List<com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition> fetchByStatus(String... values) {
        return fetch(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION.STATUS, values);
    }
}
