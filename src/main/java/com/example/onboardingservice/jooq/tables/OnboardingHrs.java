/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables;


import com.example.onboardingservice.jooq.Keys;
import com.example.onboardingservice.jooq.Public;
import com.example.onboardingservice.jooq.tables.records.OnboardingHrsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Collection;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class OnboardingHrs extends TableImpl<OnboardingHrsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.onboarding_hrs</code>
     */
    public static final OnboardingHrs ONBOARDING_HRS = new OnboardingHrs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OnboardingHrsRecord> getRecordType() {
        return OnboardingHrsRecord.class;
    }

    /**
     * The column <code>public.onboarding_hrs.chat_id</code>.
     */
    public final TableField<OnboardingHrsRecord, Long> CHAT_ID = createField(DSL.name("chat_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.onboarding_hrs.username</code>.
     */
    public final TableField<OnboardingHrsRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.onboarding_hrs.first_name</code>.
     */
    public final TableField<OnboardingHrsRecord, String> FIRST_NAME = createField(DSL.name("first_name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.onboarding_hrs.last_name</code>.
     */
    public final TableField<OnboardingHrsRecord, String> LAST_NAME = createField(DSL.name("last_name"), SQLDataType.VARCHAR(255), this, "");

    private OnboardingHrs(Name alias, Table<OnboardingHrsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private OnboardingHrs(Name alias, Table<OnboardingHrsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.onboarding_hrs</code> table reference
     */
    public OnboardingHrs(String alias) {
        this(DSL.name(alias), ONBOARDING_HRS);
    }

    /**
     * Create an aliased <code>public.onboarding_hrs</code> table reference
     */
    public OnboardingHrs(Name alias) {
        this(alias, ONBOARDING_HRS);
    }

    /**
     * Create a <code>public.onboarding_hrs</code> table reference
     */
    public OnboardingHrs() {
        this(DSL.name("onboarding_hrs"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<OnboardingHrsRecord> getPrimaryKey() {
        return Keys.ONBOARDING_HRS_PKEY;
    }

    @Override
    public OnboardingHrs as(String alias) {
        return new OnboardingHrs(DSL.name(alias), this);
    }

    @Override
    public OnboardingHrs as(Name alias) {
        return new OnboardingHrs(alias, this);
    }

    @Override
    public OnboardingHrs as(Table<?> alias) {
        return new OnboardingHrs(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public OnboardingHrs rename(String name) {
        return new OnboardingHrs(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public OnboardingHrs rename(Name name) {
        return new OnboardingHrs(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public OnboardingHrs rename(Table<?> name) {
        return new OnboardingHrs(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingHrs where(Condition condition) {
        return new OnboardingHrs(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingHrs where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingHrs where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingHrs where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingHrs where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingHrs where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingHrs where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingHrs where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingHrs whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingHrs whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
