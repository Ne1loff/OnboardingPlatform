<script lang="ts">
    import {
        createConnection,
        createEdge,
        getButtonNodeId,
        getEdgeId,
    } from "$lib/scenaries/scenarios.flow";
    import type {
        ActionButtonType,
        SendMessageActionType,
    } from "$lib/scenaries/types/scenarios.types";
    import { addEdge, useEdges, useNodes, useSvelteFlow } from "@xyflow/svelte";
    import {
        Button,
        Select,
        SelectItem,
        TextInput,
    } from "carbon-components-svelte";
    import type { DropdownItem } from "carbon-components-svelte/src/Dropdown/Dropdown.svelte";
    import ArrowRight from "carbon-icons-svelte/lib/ArrowRight.svelte";
    import CloseLarge from "carbon-icons-svelte/lib/CloseLarge.svelte";
    import { createEventDispatcher } from "svelte";
    import type { Writable } from "svelte/store";
    import { getSelectableActions } from "./utils";

    export let buttonData: Writable<ActionButtonType>;
    export let parent: Writable<SendMessageActionType>;
    export let index: number;

    const dispatch = createEventDispatcher();
    const { getEdge } = useSvelteFlow();
    const actions = useNodes();
    const edges = useEdges();

    let items: DropdownItem[];
    $: selectedAction = $buttonData.nextActionId;
    $: updateParent($buttonData);

    function updateParent(buttonData: ActionButtonType) {
        const button  = $parent.buttons[index];
        button.name = buttonData.name;
        button.nextActionId = buttonData.nextActionId;
    }

    const selectAction = (id: string) => {
        const prevConnectedNodeId = $buttonData.nextActionId;
        
        buttonData.update((it) => {
            it.nextActionId = id;
            return it;
        });

        const buttonId = getButtonNodeId($parent.id, index);
        const oldEdgeId = getEdgeId(buttonId, prevConnectedNodeId);

        if (id === "0") {
            edges.update((old) => old.filter((it) => it.id !== oldEdgeId));
            return;
        }

        const oldEdge = getEdge(oldEdgeId);
        if (oldEdge) {
            const newConnection = createConnection(buttonId, id);
            edges.update((old) => {
                const updated = old.filter((it) => it.id !== oldEdge.id);
                updated.push({
                    ...oldEdge,
                    id: getEdgeId(buttonId, $buttonData.nextActionId),
                    source: newConnection.source,
                    target: newConnection.target,
                    sourceHandle: newConnection.sourceHandle,
                    targetHandle: newConnection.targetHandle,
                });

                return updated;
            });
        } else {
            $edges = addEdge(
                createEdge(createConnection(buttonId, id)),
                $edges,
            );
        }
    };

    $: items = getSelectableActions($actions, { id: "0", text: "Выберите следующее действие" });
</script>

<div class="wrapper">
    <div class="action-button-node">
        <TextInput
            hideLabel
            labelText="Названеи"
            placeholder="Введите название..."
            bind:value={$buttonData.name}
        />
        <Button
            iconDescription="Удалить"
            size="small"
            kind="danger-tertiary"
            icon={CloseLarge}
            on:click={() => dispatch("delete", index)}
        />
    </div>
    <ArrowRight size={32} />
    <Select
        inline
        hideLabel
        labelText="➞"
        selected={selectedAction}
        on:update={({ detail }) => selectAction(detail.toString())}
    >
        {#each items as item}
            <SelectItem
                value={item.id}
                text={item.text}
                disabled={item.disabled}
            />
        {/each}
    </Select>
</div>

<style>
    .wrapper {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 8px;
    }

    .action-button-node {
        flex-grow: 1;
        border: 1px solid black;
        border-radius: 4px;
        padding: 8px;
        background-color: var(--cds-ui-background, #fff);

        display: flex;
        justify-content: space-around;
        align-items: center;
    }

    .action-inner-container {
        min-height: 20px;
        min-width: 120px;
        width: 100%;
        height: 100%;

        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
