<script lang="ts">
    import { goto, invalidate } from "$app/navigation";

    import { page } from "$app/stores";
    import type { DocumentType } from "$lib/types/documents/types";
    import {
        ComposedModal,
        ModalBody,
        ModalFooter,
        ModalHeader,
        TextInput,
    } from "carbon-components-svelte";
    import { writable } from "svelte/store";
    import type { PageData } from "./$types";

    export let data: PageData;

    const regex = new RegExp("^(?<fileName>.*)(?<extension>\\.[^.]{2,})$");
    const groups = regex.exec(data.document.filename)?.groups;

    const extension = groups ? groups["extension"] : "";
    const fileName = groups ? groups["fileName"] : "";
    const fileNameCopy = writable(fileName);

    function close() {
        goto("/documents");
    }

    async function handleSubmit() {
        const data = {
            id: $page.params.documentId,
            filename: $fileNameCopy + extension,
        } satisfies DocumentType;

        const response = await fetch(
            `/api/v1/documents/${$page.params.documentId}`,
            {
                method: "PUT",
                body: JSON.stringify(data),
                headers: {
                   "Content-Type": "application/json"
                }
            },
        );

        if (response.status === 200) {
            invalidate("app:documents").then(close);
            return;
        }
    
    }

    $: wasChanges = $fileNameCopy !== fileName;
</script>

<ComposedModal
    open={true}
    on:close={close}
    on:submit={handleSubmit}
    selectorPrimaryFocus="#document-filename"
>
    <ModalHeader
        label={"Редактровать файл: " + data.document.id}
        title={"Файл: " + data.document.filename}
    />
    <ModalBody hasForm>
        <TextInput
            id="document-filename"
            labelText="Навзание"
            placeholder="Введите название сценария"
            bind:value={$fileNameCopy}
        />
    </ModalBody>
    <ModalFooter
        primaryButtonText="Сохранить"
        primaryButtonDisabled={!wasChanges}
        secondaryButtonText="Отмена"
        on:click:button--secondary={close}
    />
</ComposedModal>
