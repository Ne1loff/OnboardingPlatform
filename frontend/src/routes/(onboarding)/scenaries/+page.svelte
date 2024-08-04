<script lang="ts">
    import {
        Button,
        DataTable,
        Link,
        Pagination,
        Toolbar,
        ToolbarContent,
        ToolbarSearch,
    } from "carbon-components-svelte";
    import Add from "carbon-icons-svelte/lib/Add.svelte";
    import Launch from "carbon-icons-svelte/lib/Launch.svelte";

    import { page } from "$app/stores";
    import type { ScenariosType } from "$lib/scenaries/types/scenarios.types";
    import { getContext } from "svelte";
    import { derived, type Writable } from "svelte/store";

    const scenaries = getContext<Writable<ScenariosType[]>>("scenaries");
    const rows = derived(scenaries, ($scenaries) =>
        $scenaries.map((it) => ({
            id: it.id,
            name: it.name,
            firstActionName:
                it.route.actions.find(
                    (action) => action.id === it.firstActionId,
                )?.name ?? "Не выбрано первое действие",
            matcherType: it.matcher.type,
            matcherValue: it.matcher.value,
            status: it.status,
        })),
    );

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
    let searchValue: string = $page.url.searchParams.get("search") ?? "";
</script>

<svelte:head>
    <title>Сценарии ондординга</title>
</svelte:head>

<DataTable
    sortable
    title="Сценарии онбординга"
    description="Все доступные сценарии для онбординга"
    headers={[
        { key: "name", value: "Название" },
        { key: "firstActionName", value: "Первое действие", sort: false },
        { key: "matcherType", value: "Тип сопоставления" },
        { key: "matcherValue", value: "Значение сопоставления", sort: false },
        { key: "status", value: "Статус" },
    ]}
    rows={$rows}
    {pageSize}
    page={pageIndex}
>
    <strong slot="title">Сценарии онбординга</strong>
    <span slot="description" style="font-size: 1rem">
        Все доступные сценарии для онбординга.
    </span>
    <svelte:fragment slot="cell" let:row let:cell>
        {#if cell.key === "name"}
            <Link icon={Launch} href={`/scenaries/${row.id}`}>
                {cell.value}
            </Link>
        {:else}
            {cell.value}
        {/if}
    </svelte:fragment>
    <Toolbar>
        <ToolbarContent>
            <ToolbarSearch
                value={searchValue}
                shouldFilterRows
                bind:filteredRowIds
            />
            <Button href="/scenaries/create" icon={Add}>Создать сценарий</Button
            >
        </ToolbarContent>
    </Toolbar>
</DataTable>
<Pagination
    pageSizeInputDisabled
    bind:pageSize
    bind:page={pageIndex}
    totalItems={filteredRowIds.length}
    itemRangeText={(min, max, total) => `${min}–${max} из ${total} ${formatTotalSelected(total, ['сценария', 'сценариев', 'сценариев'])}`}
    pageRangeText={(current, total) => `из ${total} ${formatTotalSelected(total, ['страницы', 'страниц', 'страниц'])}`}
    forwardText="Следущая страница"
    backwardText="Предылущая страница"
/>
