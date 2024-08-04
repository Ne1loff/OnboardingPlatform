<script lang="ts">
    import type { ReadMessageActionType } from "$lib/scenaries/types/scenarios.types";
    import {
        FormGroup,
        SelectItem,
        TextArea,
        TextInput,
        TimePicker,
        TimePickerSelect,
        Toggle,
    } from "carbon-components-svelte";
    import { writable, type Writable } from "svelte/store";
    import type { NotificationType } from "./types";

    export let action: Writable<ReadMessageActionType>;

    const updateNotification = (notification: NotificationType) => {
        action.update((it) => {
            it.notificationMode = notification.enable
                ? notification.mode
                : "DISABLED";
            it.waitingTime = notification.enable
                ? `PT${notification.duration}${notification.stage === "min" ? "M" : "H"}`
                : null;

            return it;
        });
    };

    const parseNotification = (): NotificationType => {
        const isDisabled = $action.notificationMode === "DISABLED";
        const mode =
            $action.notificationMode === "DISABLED"
                ? "EVERY"
                : $action.notificationMode;

        if ($action.waitingTime) {
            const regex = new RegExp("PT(?<duration>[0-9]+)(?<stage>M|H)");
            const groups = regex.exec($action.waitingTime)?.groups;
            const duration = groups ? groups["duration"] : "5";
            const stage = groups
                ? groups["stage"] === "M"
                    ? "min"
                    : "hour"
                : "min";

            return {
                enable: !isDisabled,
                mode: mode,
                duration,
                stage,
            } satisfies NotificationType;
        }

        return {
                enable: false,
                mode: mode,
                duration: null,
                stage: "min",
            } satisfies NotificationType;
    };

    const notification = writable<NotificationType>(parseNotification());

    $: {
        updateNotification($notification);
    }
</script>

<div class="container">
    <FormGroup legendText="Данные действия">
        <TextInput
            labelText="Названеи переменной"
            placeholder="Введите название переменной..."
            helperText="В нее запишется текст сообщения пользователя"
            bind:value={$action.property}
        />
        <TextInput
            labelText="Регулярное выражение"
            placeholder="Введите выражение..."
            helperText="Для выделения фильтрации текста"
            bind:value={$action.regex}
        />
    </FormGroup>
    <FormGroup legendText="Напоминание">
        <Toggle
            labelText="Включить напоминание"
            bind:toggled={$notification.enable}
        />
    </FormGroup>
    <FormGroup>
        <TimePickerSelect
            bind:value={$notification.mode}
            disabled={!$notification.enable}
        >
            <SelectItem value="EVERY" text="Каждые" />
            <SelectItem value="ONCE" text="Единожды через" />
        </TimePickerSelect>
        <TimePicker
            bind:value={$notification.duration}
            hideLabel
            labelText="Время"
            placeholder="Время"
            disabled={!$notification.enable}
        >
            <TimePickerSelect
                bind:value={$notification.stage}
                disabled={!$notification.enable}
            >
                <SelectItem value="min" text="минут" />
                <SelectItem value="hour" text="часов" />
            </TimePickerSelect>
        </TimePicker>
        <TextArea
            labelText="Текст напоминания"
            placeholder="Введите текст напоминания..."
            disabled={!$notification.enable}
            bind:value={$action.timeoutMessage}
        />
    </FormGroup>
</div>

<style>
    .container {
        margin: 1rem 0;
    }
</style>
