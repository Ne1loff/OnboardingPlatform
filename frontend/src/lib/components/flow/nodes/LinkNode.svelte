<script lang="ts">
    import type {
        ActionFlow,
        ActionLinkNode,
        ActionNodeType,
    } from "$lib/scenaries/types/scenarios.node.types";
    import type { ActionLinkType } from "$lib/scenaries/types/scenarios.types";
    import {
        Handle,
        Position,
        useNodesData,
        type NodeProps,
    } from "@xyflow/svelte";

    $$restProps;
    type $$Props = NodeProps<ActionLinkNode>;

    export let data: ActionFlow<ActionLinkType>;
    const { flow } = data;

    const link = useNodesData<ActionNodeType>($flow.link);
    const linkFlow = $link?.data.flow;
    

</script>

<div class="action-link-node">
    <div class="action-inner-container">
        <div>{$linkFlow?.name}</div>
    </div>

    <Handle type="target" position={Position.Top} />
</div>

<style>
    :global(.svelte-flow .svelte-flow__handle) {
        width: 15px;
        height: 15px;
        border-radius: 50%;
        background-color: #784be8;
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

    .action-link-node {
        border: 1px solid black;
        border-radius: 4px;
        padding: 8px;
        background-color: var(--cds-ui-background, #fff);
    }

    .action-inner-container {
        min-height: 30px;
        min-width: 150px;
        width: 100%;
        height: 100%;

        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
