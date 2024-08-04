<script lang="ts">
    import type { ScenariosType } from "$lib/scenaries/types/scenarios.types";
    import { setContext } from "svelte";
    import { writable, type Writable } from "svelte/store";
    import type { LayoutData } from "./$types";
    export let data: LayoutData;

    const scenaries = writable<ScenariosType[]>(data.scenaries);

    setContext("scenaries", scenaries);
    $: scenaries.set(data.scenaries);
</script>

<slot />
