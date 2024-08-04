<script lang="ts">
    import {
        ComposedModal,
        DataTable,
        ModalBody,
        ModalFooter,
        ModalHeader,
        Pagination,
        Toolbar,
        ToolbarBatchActions,
        ToolbarContent,
        ToolbarSearch,
    } from "carbon-components-svelte";

    import { pushState } from "$app/navigation";
    import { resolveRoute } from "$app/paths";
    import { page } from "$app/stores";
    import type {
        ActionType,
        ScenariosType,
        SendFileActionType,
    } from "$lib/scenaries/types/scenarios.types";
    import type { DocumentsType } from "$lib/types/documents/types";
    import { getContext, onMount } from "svelte";
    import { derived, get, writable, type Writable } from "svelte/store";
    import type { PageData } from "./$types";

    export let data: PageData;

    const scenarios = getContext<Writable<ScenariosType>>("currentScenarios");
    const actionsState =
        getContext<Map<string, Writable<ActionType>>>("actionsState");
    const action: Writable<SendFileActionType> = writable();

    const fileNameExtensionSplitter = new RegExp(
        "^(?<fileName>.*)\\.(?<extension>[^.]{2,})$",
    );
    const documents = writable<DocumentsType>([]);
    $: documents.set(data.documents);

    const rows = derived(documents, ($documents) =>
        $documents.map((it) => {
            const groups = fileNameExtensionSplitter.exec(it.filename)?.groups;

            const extension = groups ? groups["extension"] : "";
            const fileName = groups ? groups["fileName"] : "";
            return {
                id: it.id,
                key: it.id,
                name: fileName,
                extension: extension,
            };
        }),
    );

    function close() {
        pushState(
            resolveRoute("/scenaries/[scenarios]", {
                scenarios: $scenarios.id,
            }),
            { showFileSelectModal: undefined },
        );
    }

    async function submit() {
        action.update((it) => {
            if (!it) {
                return it;
            }

            const document = $documents.find(
                (it) => it.id === selectedRowIds[0],
            )!;
            return {
                ...it,
                fileId: document.id,
                fileName: document.filename,
            };
        });

        close();
    }

    function formatTotalSelected(total: number, titles: string[]): string {
        const indexes = [2, 0, 1, 1, 1, 2];
        const text =
            titles[
                total % 100 > 4 && total % 100 < 20
                    ? 2
                    : indexes[Math.min(total % 10, 5)]
            ];
        return text;
    }

    let pageSize = 10;
    let pageIndex = 1;
    let filteredRowIds: string[] = [];
    let selectedRowIds: string[] = [];
    let searchValue: string = $page.url.searchParams.get("search") ?? "";

    $: primaryButtonIsDisabled = !selectedRowIds.length;

    onMount(() => {
        const optionalAction = <Writable<SendFileActionType>>(
            actionsState.get(
                $page.state.showFileSelectModal?.actionStateContextId ?? "",
            )
        );

        if (!optionalAction) {
            return;
        }

        action.set(get(optionalAction));
        action.subscribe((it) => optionalAction.set(it));
    });
</script>

<ComposedModal open={true} on:close={close} on:submit={submit}>
    <ModalHeader label="Выбор файлов" title="Выберите файл для отправки" />
    <ModalBody style="overflow: hidden; min-height: 620px">
        <DataTable
            sortable
            radio
            bind:selectedRowIds
            headers={[
                { key: "key", value: "Ключ в хранилище", sort: false },
                { key: "name", value: "Название файла" },
                { key: "extension", value: "Расширение файла" },
            ]}
            rows={$rows}
            {pageSize}
            page={pageIndex}
        >
            <Toolbar>
                <ToolbarBatchActions
                    formatTotalSelected={(total) =>
                        `${formatTotalSelected(total, ["Выбран", "Выбрано", "Выбрано"])} ${total} ${formatTotalSelected(total, ["файл", "файла", "файлов"])}`}
                >
                    <svelte:fragment slot="cancel">Отменить</svelte:fragment>
                </ToolbarBatchActions>
                <ToolbarContent>
                    <ToolbarSearch
                        value={searchValue}
                        shouldFilterRows
                        bind:filteredRowIds
                    />
                </ToolbarContent>
            </Toolbar>
        </DataTable>
        <Pagination
            pageSizeInputDisabled
            bind:pageSize
            bind:page={pageIndex}
            totalItems={filteredRowIds.length}
            itemRangeText={(min, max, total) =>
                `${min}–${max} из ${total} ${formatTotalSelected(total, ["документа", "документов", "документов"])}`}
            pageRangeText={(current, total) =>
                `из ${total} ${formatTotalSelected(total, ["страницы", "страниц", "страниц"])}`}
            forwardText="Следущая страница"
            backwardText="Предылущая страница"
        />
    </ModalBody>
    <ModalFooter
        primaryButtonText="Выбрать"
        primaryButtonDisabled={primaryButtonIsDisabled}
        secondaryButtonText="Отмена"
        on:click:button--secondary={close}
    />
</ComposedModal>
