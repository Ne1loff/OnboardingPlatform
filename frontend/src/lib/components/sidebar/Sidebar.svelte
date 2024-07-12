<script lang="ts">
    import type { ActionNodeType } from "$lib/scenaries/types/scenarios.node.types";
    import type { ActionsType } from "$lib/scenaries/types/scenarios.types";
    import {
        ActionRecord,
        SendMessageActionRecord,
    } from "$lib/scenaries/types/scenarios.types.records";
    import {
        Checkbox,
        ModalBody,
        ModalFooter,
        ModalHeader,
        TextInput,
    } from "carbon-components-svelte";
    import { createEventDispatcher, setContext } from "svelte";
    import { writable, type Updater, type Writable } from "svelte/store";
    import SendMessageEditor from "./SendMessageEditor.svelte";

    export let open: boolean;
    export let node: ActionNodeType;

    let flow: Writable<ActionsType>;
    $: flow = node.data.flow;

    let checked = false;

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

    const updateNodeData = (updater: Updater<ActionsType>) => {
        if (!ActionRecord.guard($flow)) return;

        flow.update(updater);
    };
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
<div class:bx--modal-custom={true}>
    <div role="dialog" aria-modal="true" class:bx--modal-container={true}>
        {#if ActionRecord.guard($flow)}
            <ModalHeader label={$flow.id + ""} title="Подтвердите изменения" />
            <ModalBody hasForm hasScrollingContent>
                <TextInput
                    labelText="Название действия"
                    placeholder="Введите название"
                    value={$flow.name}
                    on:input={({ detail }) =>
                        updateNodeData((action) => {
                            action.name = detail + "";
                            return action;
                        })}
                />
                {#if SendMessageActionRecord.guard($flow)}
                    <SendMessageEditor action={flow} />
                {/if}
                <Checkbox
                    labelText="I have reviewed the changes"
                    bind:checked
                />
            </ModalBody>
            <ModalFooter
                secondaryButtonText="Отменить"
                primaryButtonText="Сохранить"
                primaryButtonDisabled={!checked}
            />
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
