<script lang="ts">
    import ScenariosFlow from "$lib/components/ScenariosFlow.svelte";
    import type {
        ActionType,
        ScenariosType,
    } from "$lib/scenaries/types/scenarios.types";
    import { getContext, setContext } from "svelte";
    import { writable, type Writable } from "svelte/store";
    import type { LayoutData } from "./$types";
    import type { HrsType } from "$lib/types/hrs/types";

    export let data: LayoutData;

    const scenarios = getContext<Writable<ScenariosType>>("currentScenarios");
    $: scenarios.update((it) =>
        it
            ? it.id === data.scenarios.id
                ? it
                : data.scenarios
            : data.scenarios,
    );
    $: console.log(data.scenarios);

    const actionsState: Map<string, Writable<ActionType>> = new Map();
    const hrs: Writable<HrsType> = writable(data.hrs);

    setContext("actionsState", actionsState);
    setContext("hrs", hrs);
</script>

<ScenariosFlow />

<slot />
