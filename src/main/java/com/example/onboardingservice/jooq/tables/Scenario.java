/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables;


import com.example.onboardingservice.jooq.Keys;
import com.example.onboardingservice.jooq.Public;
import com.example.onboardingservice.jooq.tables.records.ScenarioRecord;
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
public class Scenario extends TableImpl<ScenarioRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.scenario</code>
     */
    public static final Scenario SCENARIO = new Scenario();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScenarioRecord> getRecordType() {
        return ScenarioRecord.class;
    }

    /**
     * The column <code>public.scenario.id</code>.
     */
    public final TableField<ScenarioRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.scenario.scenario_name</code>.
     */
    public final TableField<ScenarioRecord, String> SCENARIO_NAME = createField(DSL.name("scenario_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.scenario.chat_id</code>.
     */
    public final TableField<ScenarioRecord, Long> CHAT_ID = createField(DSL.name("chat_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.scenario.current_action_id</code>.
     */
    public final TableField<ScenarioRecord, UUID> CURRENT_ACTION_ID = createField(DSL.name("current_action_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.scenario.is_active</code>.
     */
    public final TableField<ScenarioRecord, Boolean> IS_ACTIVE = createField(DSL.name("is_active"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.scenario.first_action_id</code>.
     */
    public final TableField<ScenarioRecord, UUID> FIRST_ACTION_ID = createField(DSL.name("first_action_id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.scenario.context</code>.
     */
    public final TableField<ScenarioRecord, JSONB> CONTEXT = createField(DSL.name("context"), SQLDataType.JSONB, this, "");

    private Scenario(Name alias, Table<ScenarioRecord> aliased) {
        this(alias, aliased, null);
    }

    private Scenario(Name alias, Table<ScenarioRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.scenario</code> table reference
     */
    public Scenario(String alias) {
        this(DSL.name(alias), SCENARIO);
    }

    /**
     * Create an aliased <code>public.scenario</code> table reference
     */
    public Scenario(Name alias) {
        this(alias, SCENARIO);
    }

    /**
     * Create a <code>public.scenario</code> table reference
     */
    public Scenario() {
        this(DSL.name("scenario"), null);
    }

    public <O extends Record> Scenario(Table<O> child, ForeignKey<O, ScenarioRecord> key) {
        super(child, key, SCENARIO);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<ScenarioRecord> getPrimaryKey() {
        return Keys.SCENARIO_PKEY;
    }

    @Override
    public Scenario as(String alias) {
        return new Scenario(DSL.name(alias), this);
    }

    @Override
    public Scenario as(Name alias) {
        return new Scenario(alias, this);
    }

    @Override
    public Scenario as(Table<?> alias) {
        return new Scenario(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Scenario rename(String name) {
        return new Scenario(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Scenario rename(Name name) {
        return new Scenario(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Scenario rename(Table<?> name) {
        return new Scenario(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, String, Long, UUID, Boolean, UUID, JSONB> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function7<? super UUID, ? super String, ? super Long, ? super UUID, ? super Boolean, ? super UUID, ? super JSONB, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function7<? super UUID, ? super String, ? super Long, ? super UUID, ? super Boolean, ? super UUID, ? super JSONB, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
