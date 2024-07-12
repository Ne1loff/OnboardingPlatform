<script lang="ts">
    import TestFlow from "$lib/components/TestFlow.svelte";
    import { SvelteFlowProvider } from "@xyflow/svelte";
    import { AspectRatio, Column, Grid, Row } from "carbon-components-svelte";
</script>

<svelte:head>
    <title>Редактор маршрутов сценария</title>
</svelte:head>
<SvelteFlowProvider>
    <Grid fullWidth noGutter>
        <Row noGutter>
            <Column noGutter>
                <AspectRatio>
                    <TestFlow />
                </AspectRatio>
            </Column>
        </Row>
    </Grid>
</SvelteFlowProvider>

<style>
    :global(.bx--content) {
        padding-top: 0;
        padding-bottom: 0;
    }
</style>
