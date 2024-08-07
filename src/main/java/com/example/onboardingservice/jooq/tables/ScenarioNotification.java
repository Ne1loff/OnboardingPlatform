/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables;


import com.example.onboardingservice.jooq.Keys;
import com.example.onboardingservice.jooq.Public;
import com.example.onboardingservice.jooq.tables.records.ScenarioNotificationRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ScenarioNotification extends TableImpl<ScenarioNotificationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.scenario_notification</code>
     */
    public static final ScenarioNotification SCENARIO_NOTIFICATION = new ScenarioNotification();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ScenarioNotificationRecord> getRecordType() {
        return ScenarioNotificationRecord.class;
    }

    /**
     * The column <code>public.scenario_notification.id</code>.
     */
    public final TableField<ScenarioNotificationRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.scenario_notification.chat_id</code>.
     */
    public final TableField<ScenarioNotificationRecord, Long> CHAT_ID = createField(DSL.name("chat_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.scenario_notification.message</code>.
     */
    public final TableField<ScenarioNotificationRecord, String> MESSAGE = createField(DSL.name("message"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.scenario_notification.repeat</code>.
     */
    public final TableField<ScenarioNotificationRecord, Boolean> REPEAT = createField(DSL.name("repeat"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.scenario_notification.delay</code>.
     */
    public final TableField<ScenarioNotificationRecord, String> DELAY = createField(DSL.name("delay"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.scenario_notification.status</code>.
     */
    public final TableField<ScenarioNotificationRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.scenario_notification.next_execution</code>.
     */
    public final TableField<ScenarioNotificationRecord, OffsetDateTime> NEXT_EXECUTION = createField(DSL.name("next_execution"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");

    private ScenarioNotification(Name alias, Table<ScenarioNotificationRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private ScenarioNotification(Name alias, Table<ScenarioNotificationRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.scenario_notification</code> table
     * reference
     */
    public ScenarioNotification(String alias) {
        this(DSL.name(alias), SCENARIO_NOTIFICATION);
    }

    /**
     * Create an aliased <code>public.scenario_notification</code> table
     * reference
     */
    public ScenarioNotification(Name alias) {
        this(alias, SCENARIO_NOTIFICATION);
    }

    /**
     * Create a <code>public.scenario_notification</code> table reference
     */
    public ScenarioNotification() {
        this(DSL.name("scenario_notification"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<ScenarioNotificationRecord> getPrimaryKey() {
        return Keys.SCENARIO_NOTIFICATION_PKEY;
    }

    @Override
    public ScenarioNotification as(String alias) {
        return new ScenarioNotification(DSL.name(alias), this);
    }

    @Override
    public ScenarioNotification as(Name alias) {
        return new ScenarioNotification(alias, this);
    }

    @Override
    public ScenarioNotification as(Table<?> alias) {
        return new ScenarioNotification(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ScenarioNotification rename(String name) {
        return new ScenarioNotification(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScenarioNotification rename(Name name) {
        return new ScenarioNotification(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ScenarioNotification rename(Table<?> name) {
        return new ScenarioNotification(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScenarioNotification where(Condition condition) {
        return new ScenarioNotification(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScenarioNotification where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScenarioNotification where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScenarioNotification where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScenarioNotification where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScenarioNotification where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScenarioNotification where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public ScenarioNotification where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScenarioNotification whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public ScenarioNotification whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
