/*
 * This file is generated by jOOQ.
 */
package com.example.onboardingservice.jooq.tables.records;


import com.example.onboardingservice.jooq.tables.ScenarioNotification;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.OffsetDateTime;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class ScenarioNotificationRecord extends UpdatableRecordImpl<ScenarioNotificationRecord> implements Record7<UUID, Long, String, Boolean, String, String, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.scenario_notification.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.scenario_notification.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.scenario_notification.chat_id</code>.
     */
    public void setChatId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.scenario_notification.chat_id</code>.
     */
    public Long getChatId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.scenario_notification.message</code>.
     */
    public void setMessage(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.scenario_notification.message</code>.
     */
    public String getMessage() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.scenario_notification.repeat</code>.
     */
    public void setRepeat(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.scenario_notification.repeat</code>.
     */
    public Boolean getRepeat() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>public.scenario_notification.delay</code>.
     */
    public void setDelay(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.scenario_notification.delay</code>.
     */
    public String getDelay() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.scenario_notification.status</code>.
     */
    public void setStatus(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.scenario_notification.status</code>.
     */
    public String getStatus() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.scenario_notification.next_execution</code>.
     */
    public void setNextExecution(OffsetDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.scenario_notification.next_execution</code>.
     */
    public OffsetDateTime getNextExecution() {
        return (OffsetDateTime) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, Long, String, Boolean, String, String, OffsetDateTime> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<UUID, Long, String, Boolean, String, String, OffsetDateTime> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.ID;
    }

    @Override
    public Field<Long> field2() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.CHAT_ID;
    }

    @Override
    public Field<String> field3() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.MESSAGE;
    }

    @Override
    public Field<Boolean> field4() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.REPEAT;
    }

    @Override
    public Field<String> field5() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.DELAY;
    }

    @Override
    public Field<String> field6() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.STATUS;
    }

    @Override
    public Field<OffsetDateTime> field7() {
        return ScenarioNotification.SCENARIO_NOTIFICATION.NEXT_EXECUTION;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getChatId();
    }

    @Override
    public String component3() {
        return getMessage();
    }

    @Override
    public Boolean component4() {
        return getRepeat();
    }

    @Override
    public String component5() {
        return getDelay();
    }

    @Override
    public String component6() {
        return getStatus();
    }

    @Override
    public OffsetDateTime component7() {
        return getNextExecution();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getChatId();
    }

    @Override
    public String value3() {
        return getMessage();
    }

    @Override
    public Boolean value4() {
        return getRepeat();
    }

    @Override
    public String value5() {
        return getDelay();
    }

    @Override
    public String value6() {
        return getStatus();
    }

    @Override
    public OffsetDateTime value7() {
        return getNextExecution();
    }

    @Override
    public ScenarioNotificationRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord value2(Long value) {
        setChatId(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord value3(String value) {
        setMessage(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord value4(Boolean value) {
        setRepeat(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord value5(String value) {
        setDelay(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord value6(String value) {
        setStatus(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord value7(OffsetDateTime value) {
        setNextExecution(value);
        return this;
    }

    @Override
    public ScenarioNotificationRecord values(UUID value1, Long value2, String value3, Boolean value4, String value5, String value6, OffsetDateTime value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScenarioNotificationRecord
     */
    public ScenarioNotificationRecord() {
        super(ScenarioNotification.SCENARIO_NOTIFICATION);
    }

    /**
     * Create a detached, initialised ScenarioNotificationRecord
     */
    public ScenarioNotificationRecord(UUID id, Long chatId, String message, Boolean repeat, String delay, String status, OffsetDateTime nextExecution) {
        super(ScenarioNotification.SCENARIO_NOTIFICATION);

        setId(id);
        setChatId(chatId);
        setMessage(message);
        setRepeat(repeat);
        setDelay(delay);
        setStatus(status);
        setNextExecution(nextExecution);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ScenarioNotificationRecord
     */
    public ScenarioNotificationRecord(com.example.onboardingservice.jooq.tables.pojos.ScenarioNotification value) {
        super(ScenarioNotification.SCENARIO_NOTIFICATION);

        if (value != null) {
            setId(value.getId());
            setChatId(value.getChatId());
            setMessage(value.getMessage());
            setRepeat(value.getRepeat());
            setDelay(value.getDelay());
            setStatus(value.getStatus());
            setNextExecution(value.getNextExecution());
            resetChangedOnNotNull();
        }
    }
}
