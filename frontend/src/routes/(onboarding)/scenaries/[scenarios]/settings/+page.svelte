<script lang="ts">
    import { goto, pushState } from "$app/navigation";
    import { resolveRoute } from "$app/paths";
    import { page } from "$app/stores";
    import { flowToScenarios } from "$lib/scenaries/scenarios.flow";
    import type { ActionsNodeType } from "$lib/scenaries/types/scenarios.node.types";
    import type {
        MatcherType,
        ScenariosStatusType,
        ScenariosType,
    } from "$lib/scenaries/types/scenarios.types";
    import { useSvelteFlow } from "@xyflow/svelte";
    import {
        Button,
        ComboBox,
        ComposedModal,
        Dropdown,
        FormGroup,
        ModalBody,
        ModalFooter,
        ModalHeader,
        RadioTile,
        TextInput,
        TileGroup,
        TooltipIcon,
    } from "carbon-components-svelte";
    import type { ComboBoxItem } from "carbon-components-svelte/src/ComboBox/ComboBox.svelte";
    import Help from "carbon-icons-svelte/lib/Help.svelte";
    import TrashCan from "carbon-icons-svelte/lib/TrashCan.svelte";
    import { getContext } from "svelte";
    import { get, writable, type Writable } from "svelte/store";

    const { getNodes } = useSvelteFlow();
    const scenarios = getContext<Writable<ScenariosType>>("currentScenarios");
    const scenariesCopy = writable<ScenariosType>({ ...$scenarios });
    const matcherCopy = writable<MatcherType>({ ...$scenariesCopy.matcher });

    function close() {
        const href = resolveRoute("/scenaries/[scenarios]", {
            scenarios: $scenarios.id,
        });

        if ($page.state.showSettings) {
            pushState(href, { showSettings: false });
        } else {
            goto(href);
        }
    }

    async function save() {
        scenarios.update((old) => ({
            ...old,
            name: $scenariesCopy.name,
            firstActionId: $scenariesCopy.firstActionId,
            matcher: {
                type: $matcherCopy.type,
                value: $matcherCopy.value,
            },
            status: $scenariesCopy.status,
        }));

        const scenario = get(scenarios);
        const nodes = <ActionsNodeType[]>getNodes();
        const scenariosFromFlow = flowToScenarios(scenario, nodes);

        const response = await fetch("/api/v1/scenarios/" + scenario.id, {
            method: "PUT",
            body: JSON.stringify(scenariosFromFlow),
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (response.status !== 200) {
            console.error(response.status, await response.json());
        }
    }

    function selectAction(id: string | undefined) {
        if (!id) return;
        $scenariesCopy.firstActionId = id;
    }

    function getActions(): ComboBoxItem[] {
        return $scenariesCopy.route.actions.map((it) => ({
            id: it.id,
            text: it.name,
        }));
    }

    function shouldFilterItem(item: ComboBoxItem, value: string): boolean {
        if (!value) return true;

        const text = item.text.toLowerCase();
        const comparison = value.toLowerCase();

        return text.includes(comparison);
    }

    const statuses: { key: ScenariosStatusType; text: string }[] = [
        { key: "PUBLISHED", text: "Опубликован" },
        { key: "TEST", text: "Тестовый режим" },
        { key: "DRAFT", text: "Черновик" },
        { key: "ARCHIVED", text: "Архивирован" },
    ];
    const statusesHelpers = {
        PUBLISHED:
            "Рабочий режим. Чат-бот функционирует согласно заданному сценарию, который предназначен для всех пользователей. ЗАПРЕЩЕНО редактирование сценария.",
        TEST: "Режим тестирования. Чат-бот функционирует по заданному сценарию, который доступен только для специальных пользователей, зарегистрированных в чат-боте как HR-специалист. РАЗРЕШЕНО редактирование сценария.",
        DRAFT: "Режим черновика. Чат-бот НЕ функционирует по заданному сценарию. РАЗРЕШЕНО редактирование сценария.",
        ARCHIVED:
            "Режим архива. Чат-бот НЕ функционирует по заданному сценарию. ЗАПРЕЩЕНО редактирование сценария.",
    };

    const items = getActions();
    $: invalid =
        $matcherCopy.type === "COMMAND" && !$matcherCopy.value.startsWith("/");
    $: helperText =
        $matcherCopy.type === "TEXT"
            ? "Для текста, есть поддержка регулярных выражений (ReGex)"
            : undefined;
</script>

<ComposedModal open={true} on:close={close} on:submit={save}>
    <ModalHeader label="Настройки" title={"Сценарий: " + $scenariesCopy.name} />
    <ModalBody hasForm style="height: 600px">
        <FormGroup>
            <TextInput
                labelText="Навзание"
                placeholder="Введите название сценария"
                bind:value={$scenariesCopy.name}
            />
            <ComboBox
                titleText="Следующее действие"
                placeholder="Нет следующего действия"
                selectedId={$scenariesCopy.firstActionId}
                {items}
                on:select={({ detail }) => selectAction(detail.selectedId)}
                on:clear={() => selectAction(undefined)}
                {shouldFilterItem}
            />
        </FormGroup>
        <FormGroup legendText="Активатор сценария">
            <Dropdown
                size="xl"
                titleText="Тип активатора"
                bind:selectedId={$matcherCopy.type}
                items={[
                    { id: "COMMAND", text: "Команда" },
                    { id: "TEXT", text: "Текст пользователся" },
                ]}
            />
            <TextInput
                labelText="Значение активатора"
                placeholder="Введите название сценария"
                {helperText}
                {invalid}
                invalidText={'Команда должжна начинаться со "/"'}
                bind:value={$matcherCopy.value}
            />
        </FormGroup>
        <FormGroup>
            <TileGroup
                legend="Статус сценария"
                name="status"
                bind:selected={$scenariesCopy.status}
            >
                {#each statuses as { key, text }}
                    <RadioTile light value={key}>
                        <div style:display="flex" style:gap="8px">
                            {text}
                            <TooltipIcon
                                tooltipText={statusesHelpers[key]}
                                icon={Help}
                            />
                        </div>
                    </RadioTile>
                {/each}
            </TileGroup>
        </FormGroup>
        <Button kind="danger-tertiary" icon={TrashCan}>Удалить сценарий</Button>
    </ModalBody>
    <ModalFooter
        primaryButtonText="Сохранить"
        secondaryButtonText="Отмена"
        on:click:button--secondary={close}
    />
</ComposedModal>
