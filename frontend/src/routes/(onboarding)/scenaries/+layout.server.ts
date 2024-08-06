import type {ScenariosType} from '$lib/scenaries/types/scenarios.types';
import {error} from '@sveltejs/kit';
import type {LayoutServerLoad} from './$types';

export const load = (async ({fetch}) => {
    const scenariosPromise = fetch("/api/v1/scenarios");
    const scenariosResponse = await scenariosPromise;
    if (scenariosResponse.status !== 200) {
        error(scenariosResponse.status, await scenariosResponse.json());
    }

    return { scenaries: <ScenariosType[]>await (scenariosResponse.json()) };
}) satisfies LayoutServerLoad;