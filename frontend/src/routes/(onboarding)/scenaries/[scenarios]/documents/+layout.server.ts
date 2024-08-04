import {resolveRoute} from '$app/paths';
import {redirect} from '@sveltejs/kit';
import type {LayoutServerLoad} from './$types';

export const load = (async ({ params, url }) => {
    if (!url.searchParams.get("from")) redirect(303, resolveRoute("/scenaries/[scenariosId]", { scenariosId: params.scenarios }))
}) satisfies LayoutServerLoad;