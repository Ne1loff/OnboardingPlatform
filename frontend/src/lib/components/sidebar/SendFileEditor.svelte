<script lang="ts">
    import { preloadData, pushState } from "$app/navigation";
    import { resolveRoute } from "$app/paths";
    import type {
        ActionType,
        ScenariosType,
        SendFileActionType,
    } from "$lib/scenaries/types/scenarios.types";
    import {
        Button,
        ContentSwitcher,
        FileUploaderItem,
        FormGroup,
        Switch,
    } from "carbon-components-svelte";
    import { Add, Upload } from "carbon-icons-svelte";
    import { getContext } from "svelte";
    import { type Writable } from "svelte/store";

    export let action: Writable<SendFileActionType>;

    const scenarios = getContext<Writable<ScenariosType>>("currentScenarios");
    const actionsState =
        getContext<Map<string, Writable<ActionType>>>("actionsState");

    let selectedIndex = 0;

    async function upload(event: MouseEvent) {
        event.preventDefault();
        const contextId = crypto.randomUUID();
        actionsState.set(contextId, action);

        pushState("", {
            showUploadModal: { actionStateContextId: contextId },
        });
    }

    async function select(event: MouseEvent) {
        event.preventDefault();
        const contextId = crypto.randomUUID();
        actionsState.set(contextId, action);

        const href = resolveRoute("/scenaries/[scenarios]/documents/select", {
            scenarios: $scenarios.id,
        });
        const result = await preloadData(`${href}?from=sidebar`);

        if (result.type === "loaded") {
            pushState(href, {
                showFileSelectModal: {
                    actionStateContextId: contextId,
                    data: result.data,
                },
            });
        }
    }
</script>

<div class="container">
    <FormGroup legendText="Файлы для отправки">
        <ContentSwitcher bind:selectedIndex>
            <Switch text="Выбрать" />
            <Switch text="Загрузить" />
        </ContentSwitcher>
    </FormGroup>
    <FormGroup>
        {#if selectedIndex === 0}
            <!-- TODO: Get from context -->
            <Button
                href="#"
                icon={Add}
                on:click={select}
                style="max-width: unset;width: 100%;"
                >Выбрать из базы документов</Button
            >
        {:else}
            <Button
                style="max-width: unset;width: 100%;"
                href={resolveRoute(
                    "/scenaries/[scenariosId]/documents/upload",
                    { scenariosId: $scenarios.id },
                )}
                on:click={upload}
                icon={Upload}>Загрузить</Button
            >
        {/if}
    </FormGroup>
    <FormGroup legendText="Загружаемый файл">
        {#if $action.fileId && $action.fileName}
            <FileUploaderItem
                style="max-width: unset;width: 100%;border: 1px solid #0f62fe;"
                status="edit"
                name={$action.fileName}
                id={$action.fileId}
                on:delete={() =>
                    action.update((it) => ({
                        ...it,
                        fileId: null,
                        fileName: null,
                    }))}
            />
        {/if}
    </FormGroup>
</div>

<style>
    .container {
        margin: 1rem 0;
    }
</style>
