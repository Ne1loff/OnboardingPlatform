<script lang="ts">
    import type {
        ActionFlow,
        ActionsNodeType,
    } from "$lib/scenaries/types/scenarios.node.types";
    import type { ActionsType } from "$lib/scenaries/types/scenarios.types";
    import { ActionButtonRecord } from "$lib/scenaries/types/scenarios.types.records";
    import {
        Handle,
        Position,
        useHandleConnections,
        useNodes,
        type NodeProps,
    } from "@xyflow/svelte";
    import { derived } from "svelte/store";

    $$restProps;
    type $$Props = NodeProps<ActionsNodeType>;

    export let id: $$Props["id"];
    export let data: ActionFlow<ActionsType>;

    const nodes = useNodes();
    const node = $nodes.find((it) => it.id === id)!;
    const parent = derived(
        nodes,
        ($nodes) => $nodes.find((it) => it.id === node.parentId),
    );
    const parentWidth = derived(parent, ($parent) => $parent?.measured?.width ? $parent.measured.width - 30 : undefined);
    const { flow } = data;
    const sourceConnections = useHandleConnections({
        nodeId: id,
        type: "source",
    });

    $: sourceIsConnectable = $sourceConnections.length === 0;
</script>

<div
    class="action-button-node"
    style:width={$parentWidth ? `${$parentWidth}px` : $parentWidth}
>
    <div class="action-inner-container">
        {#if ActionButtonRecord.guard($flow)}
            <div>{$flow.name}</div>
        {:else}
            <div>Стартовая точка</div>
        {/if}
    </div>

    <Handle
        type="source"
        position={Position.Right}
        isConnectable={sourceIsConnectable}
    />
</div>

<style>
    :global(.svelte-flow .svelte-flow__handle) {
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background-color: #784be8;
    }

    :global(.svelte-flow .action-button-node .svelte-flow__handle-right) {
        top: 18px;
    }

    :global(.svelte-flow .action-button-node .svelte-flow__handle-left) {
        top: 18px;
    }

    :global(.svelte-flow .svelte-flow__node) {
        border-width: 2px;
        font-weight: 700;
    }

    :global(
            .svelte-flow .svelte-flow__edge path,
            .svelte-flow__connectionline path
        ) {
        stroke-width: 2;
    }

    .action-button-node {
        border: 1px solid black;
        border-radius: 4px;
        padding: 8px;
        background-color: var(--cds-ui-background, #fff);
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
