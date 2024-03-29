/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables;


import com.example.onboardingservice.jooq.Keys;
import com.example.onboardingservice.jooq.Public;
import com.example.onboardingservice.jooq.tables.records.ScenarioRouteDefinitionRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.UUID;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ScenarioRouteDefinition extends TableImpl<ScenarioRouteDefinitionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.scenario_route_definition</code>
     */
    public static final ScenarioRouteDefinition SCENARIO_ROUTE_DEFINITION = new ScenarioRouteDefinition();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScenarioRouteDefinitionRecord> getRecordType() {
        return ScenarioRouteDefinitionRecord.class;
    }

    /**
     * The column <code>public.scenario_route_definition.scenario_name</code>.
     */
    public final TableField<ScenarioRouteDefinitionRecord, String> SCENARIO_NAME = createField(DSL.name("scenario_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.scenario_route_definition.first_action_id</code>.
     */
    public final TableField<ScenarioRouteDefinitionRecord, UUID> FIRST_ACTION_ID = createField(DSL.name("first_action_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.scenario_route_definition.route_source</code>.
     */
    public final TableField<ScenarioRouteDefinitionRecord, JSONB> ROUTE_SOURCE = createField(DSL.name("route_source"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>public.scenario_route_definition.matcher</code>.
     */
    public final TableField<ScenarioRouteDefinitionRecord, JSONB> MATCHER = createField(DSL.name("matcher"), SQLDataType.JSONB.nullable(false), this, "");

    private ScenarioRouteDefinition(Name alias, Table<ScenarioRouteDefinitionRecord> aliased) {
        this(alias, aliased, null);
    }

    private ScenarioRouteDefinition(Name alias, Table<ScenarioRouteDefinitionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.scenario_route_definition</code> table
     * reference
     */
    public ScenarioRouteDefinition(String alias) {
        this(DSL.name(alias), SCENARIO_ROUTE_DEFINITION);
    }

    /**
     * Create an aliased <code>public.scenario_route_definition</code> table
     * reference
     */
    public ScenarioRouteDefinition(Name alias) {
        this(alias, SCENARIO_ROUTE_DEFINITION);
    }

    /**
     * Create a <code>public.scenario_route_definition</code> table reference
     */
    public ScenarioRouteDefinition() {
        this(DSL.name("scenario_route_definition"), null);
    }

    public <O extends Record> ScenarioRouteDefinition(Table<O> child, ForeignKey<O, ScenarioRouteDefinitionRecord> key) {
        super(child, key, SCENARIO_ROUTE_DEFINITION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<ScenarioRouteDefinitionRecord> getPrimaryKey() {
        return Keys.SCENARIO_ROUTE_DEFINITION_PKEY;
    }

    @Override
    public ScenarioRouteDefinition as(String alias) {
        return new ScenarioRouteDefinition(DSL.name(alias), this);
    }

    @Override
    public ScenarioRouteDefinition as(Name alias) {
        return new ScenarioRouteDefinition(alias, this);
    }

    @Override
    public ScenarioRouteDefinition as(Table<?> alias) {
        return new ScenarioRouteDefinition(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScenarioRouteDefinition rename(String name) {
        return new ScenarioRouteDefinition(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScenarioRouteDefinition rename(Name name) {
        return new ScenarioRouteDefinition(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScenarioRouteDefinition rename(Table<?> name) {
        return new ScenarioRouteDefinition(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, UUID, JSONB, JSONB> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super String, ? super UUID, ? super JSONB, ? super JSONB, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super String, ? super UUID, ? super JSONB, ? super JSONB, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
