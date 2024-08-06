<script lang="ts">
    import type { ActionsNodeType } from "$lib/scenaries/types/scenarios.node.types";
    import {
        ActionButtonRecord,
        ActionRecord,
    } from "$lib/scenaries/types/scenarios.types.records";
    import {
        BaseEdge,
        EdgeLabelRenderer,
        getBezierPath,
        useEdges,
        useNodes,
        type Edge,
        type EdgeProps,
    } from "@xyflow/svelte";
    import { getContext } from "svelte";
    import type { Readable } from "svelte/store";

    $$restProps;
    type $$Props = EdgeProps;

    export let id: $$Props["id"];
    export let sourceX: $$Props["sourceX"];
    export let sourceY: $$Props["sourceY"];
    export let sourcePosition: $$Props["sourcePosition"];
    export let targetX: $$Props["targetX"];
    export let targetY: $$Props["targetY"];
    export let targetPosition: $$Props["targetPosition"];
    export let markerEnd: $$Props["markerEnd"] = undefined;
    export let style: $$Props["style"] = undefined;

    const disabledEditing = getContext<Readable<boolean>>(
        "scenariosDisabledEditing",
    );

    $: [edgePath, labelX, labelY] = getBezierPath({
        sourceX,
        sourceY,
        sourcePosition,
        targetX,
        targetY,
        targetPosition,
    });

    const edges = useEdges();
    const nodes = useNodes();

    const updateActionData = (
        edge: Edge<Record<string, unknown>, string | undefined>,
    ) => {
        nodes.update((it) => {
            const actionNode = <ActionsNodeType>(
                it.find((node) => node.id === edge.source)
            );
            actionNode.data.flow.update((data) => {
                if (ActionRecord.guard(data)) {
                    data.nextActionId = null;
                } else if (ActionButtonRecord.guard(data)) {
                    data.nextActionId = "0";
                }

                return data;
            });
            return it;
        });
    };

    const onEdgeClick = () =>
        edges.update((eds) =>
            eds.filter((edge) => {
                if (edge.id === id) {
                    updateActionData(edge);
                    return false;
                }
                return true;
            }),
        );
</script>

<EdgeLabelRenderer>
    <div
        class="edgeButtonContainer nodrag nopan"
        style:transform="translate(-50%, -50%) translate({labelX}px,{labelY}px)"
    >
        {#if id !== "entry-edge"}
            <button
                class="edgeButton"
                on:click={onEdgeClick}
                disabled={$disabledEditing}
            >
                ×
            </button>
        {/if}
    </div>
</EdgeLabelRenderer>
<BaseEdge path={edgePath} {markerEnd} {style} />

<style>
    .edgeButtonContainer {
        position: absolute;
        font-size: 12pt;
        /* everything inside EdgeLabelRenderer has no pointer events by default */
        /* if you have an interactive element, set pointer-events: all */
        pointer-events: all;
    }

    .edgeButton {
        width: 20px;
        height: 20px;
        background: #eee;
        border: 1px solid #fff;
        cursor: pointer;
        border-radius: 50%;
        font-size: 12px;
        line-height: 1;
    }

    .edgeButton:hover {
        box-shadow: 0 0 6px 2px rgba(0, 0, 0, 0.08);
    }
</style>
