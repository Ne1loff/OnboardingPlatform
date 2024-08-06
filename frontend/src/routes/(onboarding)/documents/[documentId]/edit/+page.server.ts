import {error} from '@sveltejs/kit';
import type {PageServerLoad} from './$types';
import type {DocumentType} from '$lib/types/documents/types';

export const load = (async ({params, fetch}) => {
    const response = await fetch(`/api/v1/documents/${params.documentId}`);
    if (response.status !== 200) {
        error(response.status, await response.json())
    }

    const document: DocumentType = await response.json();
    return { document };
}) satisfies PageServerLoad;