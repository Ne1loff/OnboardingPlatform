<script lang="ts">
    import type {
        ChangeScenariosActionType,
        ScenariosType,
    } from "$lib/scenaries/types/scenarios.types";
    import {
        ComboBox,
        FormGroup,
        RadioButton,
        RadioButtonGroup,
        TooltipIcon,
    } from "carbon-components-svelte";
    import type { ComboBoxItem } from "carbon-components-svelte/src/ComboBox/ComboBox.svelte";
    import Help from "carbon-icons-svelte/lib/Help.svelte";
    import { getContext } from "svelte";
    import { derived, type Writable } from "svelte/store";

    export let action: Writable<ChangeScenariosActionType>;

    const scenaries = getContext<Writable<ScenariosType[]>>("scenaries");
    const scenariesItems = derived<Writable<ScenariosType[]>, ComboBoxItem[]>(
        scenaries,
        ($scenaries) =>
            $scenaries.map((it) => ({ id: it.id, text: it.name })),
    );

    function shouldFilterItem(item: ComboBoxItem, value: string): boolean {
        if (!value) return true;

        const text = item.text.toLowerCase();
        const comparison = value.toLowerCase();

        return text.includes(comparison);
    }
</script>

<div class="container">
    <FormGroup>
        <ComboBox
            titleText="Запускаемый сценарий"
            placeholder="Сценарий не выбран"
            direction="top"
            selectedId={$action.nextScenariosId}
            items={$scenariesItems}
            {shouldFilterItem}
            on:select={({ detail }) =>
                ($action.nextScenariosId = detail.selectedId)}
            on:clear={() => ($action.nextScenariosId = null)}
        />
    </FormGroup>
    <FormGroup>
        <RadioButtonGroup
            orientation="vertical"
            legendText="Модификация сценария"
            name="plan-vertical"
            selected={$action.startFromBegin + ""}
            on:change={({ detail }) =>
                ($action.startFromBegin = detail === "true")}
        >
            <div slot="legendText" style:display="flex" style:gap="8px">
                Модификация сценария
                <TooltipIcon
                    tooltipText={"Параметр запуска сценария, если выбрано продолжить, сценарий продолжается с того места, где был завершен в последний раз, для конкретного пользователя"}
                    icon={Help}
                />
            </div>
            <RadioButton labelText="Начать с начала" value="true" />
            <RadioButton labelText="Продолжить" value="false" />
        </RadioButtonGroup>
    </FormGroup>
</div>

<style>
    .container {
        margin: 1rem 0;
    }
</style>
