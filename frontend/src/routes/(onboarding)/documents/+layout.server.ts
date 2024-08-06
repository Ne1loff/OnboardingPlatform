import {error} from '@sveltejs/kit';
import type {LayoutServerLoad} from './$types';
import type {DocumentsType} from '$lib/types/documents/types';

export const load = (async ({fetch, depends}) => {
    depends('app:documents');
    const response = await fetch("/api/v1/documents");
    if (response.status !== 200) {
        error(response.status, await response.json())
    }

    const documents: DocumentsType = await response.json(); 
    return { documents: documents};
}) satisfies LayoutServerLoad;

