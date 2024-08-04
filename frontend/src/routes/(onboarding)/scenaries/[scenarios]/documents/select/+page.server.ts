import type {DocumentsType} from '$lib/types/documents/types';
import {error} from '@sveltejs/kit';
import type {PageServerLoad} from './$types';

export const load = (async ({ fetch }) => {
    const response = await fetch("/api/v1/documents");
    if (response.status !== 200) {
        error(response.status, await response.json())
    }

    const documents: DocumentsType = await response.json();
    return { documents: documents };
}) satisfies PageServerLoad;