<script lang="ts">
    import type { ForwardMessageActionType, ScenariosType } from "$lib/scenaries/types/scenarios.types";
    import type { HrsType, HrType } from "$lib/types/hrs/types";
    import { ComboBox, FormGroup } from "carbon-components-svelte";
    import type { ComboBoxItem } from "carbon-components-svelte/src/ComboBox/ComboBox.svelte";
    import { getContext } from "svelte";
    import { derived, writable, type Writable } from "svelte/store";

    export let action: Writable<ForwardMessageActionType>;

    
    const hrs = getContext<Writable<HrsType>>("hrs");
    const hrsItems = derived<
        Writable<HrsType>,
        ComboBoxItem[]
    >(hrs, ($hrs) => $hrs.map((it) => ({ id: it.chatId, text: telegramUserNameMapper(it) })));

    function telegramUserNameMapper(hr: HrType): string {
        let nameInfo = "";

        if (hr.firstName) nameInfo += ` (${hr.firstName}`;
        if (hr.firstName && hr.lastName) nameInfo += ` ${hr.lastName}`;
        if (hr.firstName) nameInfo += ")"

        return `@${hr.username}${nameInfo}`;
    }

    function shouldFilterItem(item: ComboBoxItem, value: string): boolean {
        if (!value) return true;

        const text = item.text.toLowerCase();
        const comparison = value.toLowerCase();

        return text.includes(comparison);
    }

    let selectedChatId: number | undefined =
        $action.forwardChatId > 0 ? $action.forwardChatId : undefined;

    $: {
        if (selectedChatId) $action.forwardChatId = selectedChatId;
    }
</script>

<div class="container">
    <FormGroup>
        <ComboBox
            titleText="Пользователь для пересылки сообщения"
            placeholder="Пользователь не выбран"
            direction="top"
            invalid={selectedChatId === undefined}
            invalidText="Должен быть выбран чат, в который будет переотправлено сообщение"
            selectedId={selectedChatId}
            items={$hrsItems}
            {shouldFilterItem}
            on:select={({ detail }) => (selectedChatId = detail.selectedId)}
            on:clear={() => (selectedChatId = undefined)}
        />
    </FormGroup>
</div>

<style>
    .container {
        margin: 1rem 0;
    }
</style>
