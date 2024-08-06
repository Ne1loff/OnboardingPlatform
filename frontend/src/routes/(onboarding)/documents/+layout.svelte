<script lang="ts">
    import {
        Button,
        DataTable,
        OverflowMenu,
        OverflowMenuItem,
        Pagination,
        Toolbar,
        ToolbarBatchActions,
        ToolbarContent,
        ToolbarSearch,
    } from "carbon-components-svelte";
    import Upload from "carbon-icons-svelte/lib/Upload.svelte";

    import { invalidate } from "$app/navigation";
    import { resolveRoute } from "$app/paths";
    import { page } from "$app/stores";
    import type { DocumentsType } from "$lib/types/documents/types";
    import { Save, TrashCan } from "carbon-icons-svelte";
    import { derived, writable } from "svelte/store";
    import type { LayoutData } from "./$types";
    import { tick } from "svelte";

    export let data: LayoutData;

    const fileNameExtensionSplitter = new RegExp("^(?<fileName>.*)\\.(?<extension>[^.]{2,})$");
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

    function downloadDocument(event: MouseEvent, href: string) {
        event.preventDefault();

        var a = document.createElement("a");
        a.href = href;
        a.target = "_blank";
        document.body.appendChild(a);
        a.click();
        a.remove();
    }

    async function iternalDeleteFiles(documentIds: string[]) {
        const response = await fetch(
            `/api/v1/documents?documentId=${documentIds.join("&documentId=")}`,
            {
                method: "DELETE",
            },
        );

        if (response.status === 200) {
            invalidate("app:documents");
            return;
        }
        console.error(response);
    }

    function deleteSelectedFiles() {
        iternalDeleteFiles(selectedRowIds);
        selectedRowIds = [];
    }

    function deleteFile(documentId: string) {
        iternalDeleteFiles([documentId]);
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

    let pageSize = 13;
    let pageIndex = 1;
    let filteredRowIds: string[] = [];
    let selectedRowIds: string[] = [];
    let searchValue: string = $page.url.searchParams.get("search") ?? "";
</script>

<DataTable
    sortable
    batchSelection
    bind:selectedRowIds
    title="Документы"
    description="Документы загруженные в систему"
    headers={[
        { key: "key", value: "Ключ в хранилище", sort: false },
        { key: "name", value: "Название файла" },
        { key: "extension", value: "Расширение файла" },
        { key: "overflow", empty: true, width: "64px" },
    ]}
    rows={$rows}
    {pageSize}
    page={pageIndex}
>
    <strong slot="title">Документы</strong>
    <span slot="description" style="font-size: 1rem">
        Документы загруженные в систему
    </span>
    <svelte:fragment slot="cell" let:row let:cell>
        {#if cell.key === "overflow"}
            <OverflowMenu flipped>
                <OverflowMenuItem
                    href={`/api/v1/documents/download?documentId=${row.id}`}
                    text="Скачать"
                    on:click={(e) =>
                        downloadDocument(
                            e,
                            `/api/v1/documents/download?documentId=${row.id}`,
                        )}
                />
                <OverflowMenuItem
                    href={resolveRoute("/documents/[id]/edit", { id: row.id })}
                    text="Изменить"
                />
                <OverflowMenuItem
                    hasDivider
                    danger
                    text="Удалить"
                    on:click={() => deleteFile(row.id)}
                />
            </OverflowMenu>
        {:else}{cell.value}{/if}
    </svelte:fragment>
    <Toolbar>
        <ToolbarBatchActions
            formatTotalSelected={(total) =>
                `${formatTotalSelected(total, ["Выбран", "Выбрано", "Выбрано"])} ${total} ${formatTotalSelected(total, ["файл", "файла", "файлов"])}`}
        >
            <Button
                target="_blank"
                icon={Save}
                href={`/api/v1/documents/download?documentId=${selectedRowIds.join("&documentId=")}`}
                >{selectedRowIds.length > 1 ? "Скачать архив" : "Скачать"}
            </Button>
            <Button icon={TrashCan} on:click={deleteSelectedFiles} kind="danger"
                >{selectedRowIds.length > 1 ? "Удалить файлы" : "Удалить файл"}
            </Button>
            <svelte:fragment slot="cancel">Отменить</svelte:fragment>
        </ToolbarBatchActions>
        <ToolbarContent>
            <ToolbarSearch
                value={searchValue}
                shouldFilterRows
                bind:filteredRowIds
            />
            <Button href="/documents/upload" icon={Upload}>Загрузить</Button>
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

<slot />
