import {error, redirect} from '@sveltejs/kit';

import {resolveRoute} from "$app/paths";
import type {ScenariosType} from '$lib/scenaries/types/scenarios.types';
import type {PageServerLoad} from './$types';

type ScenariosCreateResponse = {
    scenariosId: string
}

export const load = (async ({ fetch }) => {
    const scenarios = {
        id: "",
        status: "DRAFT",
        name: "",
        firstActionId: null,
        matcher: {
            type: "COMMAND",
            value: ""
        },
        route: { actions: [] }
    } satisfies ScenariosType;

    const response = await fetch("/api/v1/scenarios", {
        method: "POST",
        body: JSON.stringify(scenarios),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.status !== 201) {
        error(response.status, await response.json());
    }

    const scenariosResponse = <ScenariosCreateResponse>await response.json()
    redirect(301, resolveRoute("/scenaries/[scenarios]/settings", { scenarios: scenariosResponse.scenariosId }));
}) satisfies PageServerLoad;