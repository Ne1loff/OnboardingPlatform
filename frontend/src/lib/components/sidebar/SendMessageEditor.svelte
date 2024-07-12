<script lang="ts">
    import {
        createButtonNode,
        getButtonNodeId,
    } from "$lib/scenaries/scenarios.flow";
    import type { ActionButtonNode } from "$lib/scenaries/types/scenarios.node.types";
    import type {
        ActionButtonType,
        SendMessageActionType,
    } from "$lib/scenaries/types/scenarios.types";
    import { useNodes, useSvelteFlow } from "@xyflow/svelte";
    import { Button, FormGroup } from "carbon-components-svelte";
    import type { Writable } from "svelte/store";
    import Editor from "../bytemd/svelte/editor.svelte";
    import "./../bytemd/index.css";
    import ActionButtonComponent from "./ActionButtonComponent.svelte";

    export let action: Writable<SendMessageActionType>;

    const nodes = useNodes();
    const { deleteElements } = useSvelteFlow();

    let value: string;
    $: value = $action.text;
    $action.isMarkdownText = true;

    const plugins: any[] = [];

    function handleChange(e: CustomEvent<{ value: string }>) {
        $action.text = e.detail.value;
    }

    function createButton() {
        action.update((it) => {
            const btnIndex =
                it.buttons.push({ name: "", nextActionId: "" }) - 1;
            const node = createButtonNode(
                it.buttons[btnIndex],
                it.id,
                btnIndex,
            );

            nodes.update((old) => {
                old.push(node);
                return old;
            });
            return it;
        });
    }

    function getButtonNodeDataFlow(index: number): Writable<ActionButtonType> {
        const buttonNodeId = getButtonNodeId($action.id, index);
        const buttonNode = <ActionButtonNode>(
            $nodes.find((it) => it.id === buttonNodeId)
        );

        return buttonNode.data.flow;
    }

    function deleteButton(index: number) {
        const buttonNodeId = getButtonNodeId($action.id, index);
        deleteElements({ nodes: [{ id: buttonNodeId }] }).then(() => {
            action.update((action) => {
                action.buttons.splice(index, 1);
                return action;
            });
        });
    }
</script>

<div class="container">
    <FormGroup legendText="Текст сообщения">
        <Editor {value} {plugins} on:change={handleChange} />
    </FormGroup>
</div>
<div class="container">
    <FormGroup legendText="Кнопки">
        {#each $action.buttons as { }, index}
            <ActionButtonComponent
                buttonData={getButtonNodeDataFlow(index)}
                parent={action}
                {index}
                on:delete={({ detail }) => deleteButton(detail)}
            />
        {/each}
    </FormGroup>
    <Button on:click={createButton}>Добавить</Button>
</div>

<style>
    .container {
        margin: 1rem 0;
    }
</style>
