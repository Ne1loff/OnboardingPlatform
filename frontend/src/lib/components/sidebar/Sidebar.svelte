<script lang="ts">
    import {
        ActionFlowNodeType,
        createConnection,
        createEdge,
        getEdgeId,
    } from "$lib/scenaries/scenarios.flow";
    import type { ActionNodeType } from "$lib/scenaries/types/scenarios.node.types";
    import type { ActionType } from "$lib/scenaries/types/scenarios.types";
    import { ActionRecord } from "$lib/scenaries/types/scenarios.types.records";
    import { addEdge, useEdges, useNodes, useSvelteFlow } from "@xyflow/svelte";
    import {
        ComboBox,
        ModalBody,
        ModalHeader,
        TextInput,
    } from "carbon-components-svelte";
    import type { ComboBoxItem } from "carbon-components-svelte/src/ComboBox/ComboBox.svelte";
    import { createEventDispatcher, setContext } from "svelte";
    import { writable, type Writable } from "svelte/store";
    import ChangeScenariosEditor from "./ChangeScenariosEditor.svelte";
    import ForwardMessageEditor from "./ForwardMessageEditor.svelte";
    import ReadMessageEditor from "./ReadMessageEditor.svelte";
    import SendContactEditor from "./SendContactEditor.svelte";
    import SendFileEditor from "./SendFileEditor.svelte";
    import SendMessageEditor from "./SendMessageEditor.svelte";
    import { getSelectableActions } from "./utils";

    export let open: boolean;
    export let node: ActionNodeType;

    const { getEdge } = useSvelteFlow();
    const actions = useNodes();
    const edges = useEdges();

    let flow: Writable<ActionType>;
    $: flow = node.data.flow;

    const dispatch = createEventDispatcher();
    const label = writable<string>(undefined);

    setContext("ComposedModal", {
        closeModal: () => {
            open = false;
        },
        submit: () => {
            dispatch("submit");
            dispatch("click:button--primary");
        },
        declareRef: (_it: any) => {},
        updateLabel: (value: string) => {
            label.set(value);
        },
    });

    const selectAction = (id: string | undefined) => {
        if (!ActionRecord.guard($flow)) return;

        const prevConnectedNodeId = $flow.nextActionId ?? undefined;
        $flow.nextActionId = !id ? null : id;

        if (prevConnectedNodeId) {
            const oldEdgeId = getEdgeId($flow.id, prevConnectedNodeId);

            if (!id) {
                edges.update((old) => old.filter((it) => it.id !== oldEdgeId));
                return;
            }

            const oldEdge = getEdge(oldEdgeId)!;
            const newConnection = createConnection($flow.id, id);

            edges.update((old) => {
                const updated = old.filter((it) => it.id !== oldEdge.id);
                updated.push({
                    ...oldEdge,
                    id: getEdgeId(newConnection.source, newConnection.target),
                    source: newConnection.source,
                    target: newConnection.target,
                    sourceHandle: newConnection.sourceHandle,
                    targetHandle: newConnection.targetHandle,
                });

                return updated;
            });
        } else if (id) {
            $edges = addEdge(
                createEdge(createConnection($flow.id, id)),
                $edges,
            );
        }
    };

    function shouldFilterItem(item: ComboBoxItem, value: string): boolean {
        if (!value) return true;

        const text = item.text.toLowerCase();
        const comparison = value.toLowerCase();

        return text.includes(comparison);
    }

    $: selectedAction = $flow.nextActionId ?? undefined;
    $: items = getSelectableActions($actions);

    function cast<T>(flow: Writable<ActionType>): T {
        return <T>flow;
    }
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
<div class:bx--modal-custom={true}>
    <div role="dialog" aria-modal="true" class:bx--modal-container={true}>
        {#if ActionRecord.guard($flow)}
            <ModalHeader label={$flow.id + ""} title="Изменить действие" />
            <ModalBody hasForm hasScrollingContent>
                <TextInput
                    labelText="Название действия"
                    placeholder="Введите название"
                    bind:value={$flow.name}
                />
                {#if node.type == ActionFlowNodeType.SEND_MESSAGE}
                    <SendMessageEditor action={cast(flow)} />
                {:else if node.type == ActionFlowNodeType.CHANGE_SCENARIOS}
                    <ChangeScenariosEditor action={cast(flow)} />
                {:else if node.type == ActionFlowNodeType.SEND_FILE}
                    <SendFileEditor action={cast(flow)} />
                {:else if node.type == ActionFlowNodeType.SEND_CONTACT}
                    <SendContactEditor action={cast(flow)} />
                {:else if node.type == ActionFlowNodeType.READ_MESSAGE}
                    <ReadMessageEditor action={cast(flow)} />
                {:else if node.type == ActionFlowNodeType.FORWARD_MESSAGE}
                    <ForwardMessageEditor action={cast(flow)} />
                {/if}
                <ComboBox
                    titleText="Следующее действие"
                    placeholder="Нет следующего действия"
                    direction="top"
                    selectedId={selectedAction}
                    {items}
                    on:select={({ detail }) => selectAction(detail.selectedId)}
                    on:clear={() => selectAction(undefined)}
                    {shouldFilterItem}
                />
            </ModalBody>
        {/if}
    </div>
</div>

<style>
    .bx--modal-custom {
        width: 100%;
        min-width: 400px;
        height: 100%;
        margin-top: 16px;

        max-height: 800px;
        max-width: 800px;
    }

    :global(.bx--modal-custom .bx--modal-container) {
        width: 100%;
        height: 100%;
        max-height: 800px;
        max-width: 800px;

        -webkit-box-shadow: 4px 4px 8px 0px rgba(0, 0, 0, 0.2);
        -moz-box-shadow: 4px 4px 8px 0px rgba(0, 0, 0, 0.2);
        box-shadow: 4px 4px 8px 0px rgba(0, 0, 0, 0.2);
    }
</style>
