<script lang="ts">
    import { page } from "$app/stores";
    import type { PageData } from "./$types";
    import SelectDocumentPage from "./documents/select/+page.svelte";
    import UploadDocumentPage from "./documents/upload/+page.svelte";
    import ScenariosSettingsPage from "./settings/+page.svelte";

    export let data: PageData;
</script>

<svelte:head>
    <title>Редактор маршрутов сценария: {data.scenarios.name}</title>
</svelte:head>

{#if $page.state.showFileSelectModal}
    <SelectDocumentPage data={$page.state.showFileSelectModal.data} />
{/if}
{#if $page.state.showUploadModal}
    <UploadDocumentPage />
{/if}
{#if $page.state.showSettings}
    <ScenariosSettingsPage />
{/if}
