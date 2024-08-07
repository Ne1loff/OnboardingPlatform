/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables.records;


import com.example.onboardingservice.jooq.tables.ScenarioRouteDefinition;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;

import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ScenarioRouteDefinitionRecord extends UpdatableRecordImpl<ScenarioRouteDefinitionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.scenario_route_definition.scenario_name</code>.
     */
    public void setScenarioName(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.scenario_route_definition.scenario_name</code>.
     */
    public String getScenarioName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.scenario_route_definition.first_action_id</code>.
     */
    public void setFirstActionId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.scenario_route_definition.first_action_id</code>.
     */
    public UUID getFirstActionId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.scenario_route_definition.route_source</code>.
     */
    public void setRouteSource(JSONB value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.scenario_route_definition.route_source</code>.
     */
    public JSONB getRouteSource() {
        return (JSONB) get(2);
    }

    /**
     * Setter for <code>public.scenario_route_definition.matcher</code>.
     */
    public void setMatcher(JSONB value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.scenario_route_definition.matcher</code>.
     */
    public JSONB getMatcher() {
        return (JSONB) get(3);
    }

    /**
     * Setter for <code>public.scenario_route_definition.id</code>.
     */
    public void setId(UUID value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.scenario_route_definition.id</code>.
     */
    public UUID getId() {
        return (UUID) get(4);
    }

    /**
     * Setter for <code>public.scenario_route_definition.status</code>.
     */
    public void setStatus(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.scenario_route_definition.status</code>.
     */
    public String getStatus() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScenarioRouteDefinitionRecord
     */
    public ScenarioRouteDefinitionRecord() {
        super(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION);
    }

    /**
     * Create a detached, initialised ScenarioRouteDefinitionRecord
     */
    public ScenarioRouteDefinitionRecord(String scenarioName, UUID firstActionId, JSONB routeSource, JSONB matcher, UUID id, String status) {
        super(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION);

        setScenarioName(scenarioName);
        setFirstActionId(firstActionId);
        setRouteSource(routeSource);
        setMatcher(matcher);
        setId(id);
        setStatus(status);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ScenarioRouteDefinitionRecord
     */
    public ScenarioRouteDefinitionRecord(com.example.onboardingservice.jooq.tables.pojos.ScenarioRouteDefinition value) {
        super(ScenarioRouteDefinition.SCENARIO_ROUTE_DEFINITION);

        if (value != null) {
            setScenarioName(value.getScenarioName());
            setFirstActionId(value.getFirstActionId());
            setRouteSource(value.getRouteSource());
            setMatcher(value.getMatcher());
            setId(value.getId());
            setStatus(value.getStatus());
            resetChangedOnNotNull();
        }
    }
}
