import type {ScenariosType} from '$lib/scenaries/types/scenarios.types';
import type {HrsType} from '$lib/types/hrs/types';
import {error} from '@sveltejs/kit';
import type {LayoutServerLoad} from './$types';


export const load = (async ({ params, fetch }) => {
    const scenariosPromise = fetch("/api/v1/scenarios/" + params.scenarios);
    const hrsPromise = fetch("/api/v1/hrs");

    const scenariosResponse = await scenariosPromise;
    const hrsResponse = await hrsPromise;

    if (scenariosResponse.status !== 200 || hrsResponse.status !== 200) {
        error(404, { message: "Not found" });
    }

    return {
        scenarios: <ScenariosType>await (scenariosResponse.json()),
        hrs: <HrsType>await (hrsResponse.json())
    };

}) satisfies LayoutServerLoad;