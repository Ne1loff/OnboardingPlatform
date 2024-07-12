<script lang="ts">
    import type {
        ActionFlow,
        ActionNodeType,
    } from "$lib/scenaries/types/scenarios.node.types";
    import type { ActionsType } from "$lib/scenaries/types/scenarios.types";
    import { SendMessageActionRecord } from "$lib/scenaries/types/scenarios.types.records";
    import {
        Handle,
        Position,
        useHandleConnections,
        type NodeProps,
    } from "@xyflow/svelte";

    $$restProps;
    type $$Props = NodeProps<ActionNodeType>;

    export let id: $$Props["id"];
    export let data: ActionFlow<ActionsType>;
    const { flow } = data;

    const sourceConnections = useHandleConnections({
        nodeId: id,
        type: "source",
    });

    $: sourceIsConnectable = $sourceConnections.length === 0;
    $: height =
        SendMessageActionRecord.guard($flow) && $flow.buttons.length > 0
            ? (56 + $flow.buttons.length * 42)
            : undefined;
</script>

<div class="action-node" style:height={height ? `${height}px` : height}>
    <div class="action-inner-container">
        <div>{$flow.name}</div>
    </div>

    <Handle type="target" position={Position.Left} />
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

    :global(.svelte-flow .action-node .svelte-flow__handle-right) {
        top: 25px;
    }

    :global(.svelte-flow .action-node .svelte-flow__handle-left) {
        top: 25px;
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

    .action-node {
        border: 1px solid black;
        border-radius: 4px;
        padding: 8px;
        background-color: var(--cds-ui-background, #fff);

        display: flex;
        align-items: start;
    }

    .action-inner-container {
        height: 30px;
        min-width: 150px;

        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
