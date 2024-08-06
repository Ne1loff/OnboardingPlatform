<script lang="ts">
    import { createEventDispatcher } from "svelte";
    import { wordsCount } from "./utils";

    export let showSync: boolean;
    export let value: string;
    export let syncEnabled: boolean;
    export let locale: { [k: string]: string };
    export let islimited: boolean;
    const dispatch = createEventDispatcher();

    let valueWordsCount;
    let valueWords;

    const words = (it: string) => {
        return wordsCount(it);
    };

    $: valueWordsCount = words(value);
    $: lines = value.split("\n").length;
</script>

<div class="bytemd-status">
    <div class="bytemd-status-left">
        <span>{locale.words}: <strong>{valueWordsCount}</strong></span><span
            >{locale.lines}
            : <strong>{lines}</strong></span
        >
        {#if islimited}
            <span class="bytemd-status-error">{locale.limited}</span>{/if}
    </div>
    <div class="bytemd-status-right">
        {#if showSync}
            <label>
                <input
                    type="checkbox"
                    checked={syncEnabled}
                    on:change={() => dispatch("sync", !syncEnabled)}
                />
                {locale.sync}
            </label>
        {/if}
        <span
            role="button"
            tabindex="0"
            on:click={() => dispatch("top")}
            on:keydown|self={(e) =>
                ["Enter", "Space"].includes(e.code) && dispatch("top")}
        >
            {locale.top}
        </span>
    </div>
</div>
