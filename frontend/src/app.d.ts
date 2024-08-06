// See https://kit.svelte.dev/docs/types#app

import type {ScenariosType} from "$lib/scenaries/types/scenarios.types";
import type {DocumentsType, DocumentType} from "$lib/types/documents/types";
import type {HrsType} from "$lib/types/hrs/types";

// for information about these interfaces
declare global {
	namespace App {
		// interface Error {}
		interface Locals {
			localRequest?: boolean;
		}

		interface PageData {
			scenarios?: ScenariosType;
			scenaries?: ScenariosType[];
			documents?: DocumentsType;
			document?: DocumentType;
			hrs?: HrsType
		}

		interface PageState {
			showSettings?: boolean;
			showUploadModal?: { actionStateContextId: string };
			showFileSelectModal?: { actionStateContextId: string, data: any };
		}
		// interface Platform {}
	}
}

export { };

