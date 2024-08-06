/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables;


import com.example.onboardingservice.jooq.Keys;
import com.example.onboardingservice.jooq.Public;
import com.example.onboardingservice.jooq.tables.records.OnboardingPropertiesRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Collection;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class OnboardingProperties extends TableImpl<OnboardingPropertiesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.onboarding_properties</code>
     */
    public static final OnboardingProperties ONBOARDING_PROPERTIES = new OnboardingProperties();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OnboardingPropertiesRecord> getRecordType() {
        return OnboardingPropertiesRecord.class;
    }

    /**
     * The column <code>public.onboarding_properties.name</code>.
     */
    public final TableField<OnboardingPropertiesRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.onboarding_properties.description</code>.
     */
    public final TableField<OnboardingPropertiesRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>public.onboarding_properties.value</code>.
     */
    public final TableField<OnboardingPropertiesRecord, String> VALUE = createField(DSL.name("value"), SQLDataType.VARCHAR(1024).nullable(false), this, "");

    private OnboardingProperties(Name alias, Table<OnboardingPropertiesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private OnboardingProperties(Name alias, Table<OnboardingPropertiesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.onboarding_properties</code> table
     * reference
     */
    public OnboardingProperties(String alias) {
        this(DSL.name(alias), ONBOARDING_PROPERTIES);
    }

    /**
     * Create an aliased <code>public.onboarding_properties</code> table
     * reference
     */
    public OnboardingProperties(Name alias) {
        this(alias, ONBOARDING_PROPERTIES);
    }

    /**
     * Create a <code>public.onboarding_properties</code> table reference
     */
    public OnboardingProperties() {
        this(DSL.name("onboarding_properties"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<OnboardingPropertiesRecord> getPrimaryKey() {
        return Keys.ONBOARDING_PROPERTIES_PKEY;
    }

    @Override
    public OnboardingProperties as(String alias) {
        return new OnboardingProperties(DSL.name(alias), this);
    }

    @Override
    public OnboardingProperties as(Name alias) {
        return new OnboardingProperties(alias, this);
    }

    @Override
    public OnboardingProperties as(Table<?> alias) {
        return new OnboardingProperties(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public OnboardingProperties rename(String name) {
        return new OnboardingProperties(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public OnboardingProperties rename(Name name) {
        return new OnboardingProperties(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public OnboardingProperties rename(Table<?> name) {
        return new OnboardingProperties(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingProperties where(Condition condition) {
        return new OnboardingProperties(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingProperties where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingProperties where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingProperties where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingProperties where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingProperties where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingProperties where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public OnboardingProperties where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingProperties whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public OnboardingProperties whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
